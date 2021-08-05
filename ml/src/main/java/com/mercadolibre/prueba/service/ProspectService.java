package com.mercadolibre.prueba.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.prueba.controller.ProspectController;
import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.MainServiceException;
import com.mercadolibre.prueba.errores.ProcessorException;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.processors.ToUppercaseProcessor;
import com.mercadolibre.prueba.utils.MutantConst;
import com.mercadolibre.prueba.utils.Position;
import com.mercadolibre.prueba.utils.Status;
import com.mercadolibre.prueba.validators.DNAAllowedTypesValidator;
import com.mercadolibre.prueba.validators.MinLengthValidator;
import com.mercadolibre.prueba.validators.SequenceLengthValidator;

@Service

public class ProspectService {

	@Autowired
	Status status;
	@Autowired
	SequenceLengthValidator sequenceLengthValidator;
	@Autowired
	MinLengthValidator minLengthValidator;
	@Autowired
	ToUppercaseProcessor toUppercaseProcessor;
	@Autowired
	DNAAllowedTypesValidator dNAAllowedTypesValidator;

	private long occurrencesAccumulator;

	/**
	 * mainService Servicio principal
	 * <p>
	 * 
	 * @param human instance of Human
	 */
	public boolean mainService(Human human) throws MainServiceException {

		try {
			// Validaciones
			// Largo de cada secuencia sean iguales
			if(! sequenceLengthValidator.validate(human)) {
				throw new ValidatorException("Unequal dna dimension chain");
			}

			// cantidad de sequencias mayor a 4
			if(! minLengthValidator.validate(human)) {
				throw new ValidatorException("Short dna chain");
			}
			// pasa a Mayusculas para matchear pattern
			toUppercaseProcessor.process(human);// pasa a mayusculas

			// valida que solo sean A C T G
			if(! dNAAllowedTypesValidator.validate(human)) {
				throw new ValidatorException("Not A C T G Types");
			}

			// inicializa Status y ocurrencias
			status.setConditionReached(false);
			occurrencesAccumulator = 0;

			return isMutant(human.getDna());

		} catch (ProcessorException | ValidatorException ex) {
			
			Logger.getLogger(ProspectService.class.getName()).log(Level.SEVERE, null, ex);
			throw new MainServiceException("Exception in Main Service: "+ ex.getMessage());

		}

	}

	/**
	 * Main method isMutant
	 * <p>
	 * @param dna of type String[] represents dna sequence
	 */
	public boolean isMutant(String[] dna) { // ORQUESTA TODAS LAS OPERACIONES

		status.setConditionReached(false);
		occurrencesAccumulator = 0;

		//1 -  PROCESO ROWS
		patternsChecker(dna);
		if (status.isConditionReached())
			return true;

		//2 - PROCESO COLS
		String[] cols = retrieveColumns(dna);
		patternsChecker(cols);// Pasando cols
		if (status.isConditionReached())
			return true;

		//3 - DEFINO MATRIZ
	    char[][] matrix = defineMatrix(dna);
		int size = matrix.length;
		String[] param = new String[1];

		//4 -  PROCESO DIAGONAL MAYOR DE IZQ A DERECHA
		List<Position> majorDiagonalLeftToRight = diagonalLeftToRight(size);
		String diagonalLeftToRight = retrieveFromMatrix(majorDiagonalLeftToRight, matrix);
		param[0] = diagonalLeftToRight;
		patternsChecker(param);
		if (status.isConditionReached())
			return true;

		//5 -  PROCESO DIAGONALES DERIVADAS DE LA MAYOR DE IZQ A DERECHA
		procesLeftToRightRight(majorDiagonalLeftToRight, matrix);
		if (status.isConditionReached())
			return true;

		procesLeftToRightDown(majorDiagonalLeftToRight, matrix);
		if (status.isConditionReached())
			return true;

		//6 -  PROCESO DIAGONAL MAYOR DE DERECHA A IZQ
		List<Position> majorDiagonaliagonalRightToLeft = diagonalRightToLeft(size);
		String diagonalRightToLeft = retrieveFromMatrix(majorDiagonaliagonalRightToLeft, matrix);
		param[0] = diagonalRightToLeft;
		patternsChecker(param);
		if (status.isConditionReached())
			return true;

		//7 -  PROCESO DIAGONALES DERIVADAS DE LA MAYOR DE DERECHA A IZQ
		procesRightToLeftLeft(majorDiagonaliagonalRightToLeft, matrix);
		if (status.isConditionReached())
			return true;

		procesRightToLeftDown(majorDiagonaliagonalRightToLeft, matrix);
		if (status.isConditionReached())
			return true;

		return false;
	}

	private void patternsChecker(String[] dna) {
		
		Pattern pattern = Pattern.compile(MutantConst.PATTERN_AAAA);
		occurrencesAccumulator += Arrays.stream(dna).parallel().filter(pattern.asPredicate()).count();
		if (testStatus()) {
			return;
		}
		
		pattern = Pattern.compile(MutantConst.PATTERN_CCCC);
		occurrencesAccumulator += Arrays.stream(dna).parallel().filter(pattern.asPredicate()).count();

		if (testStatus()) {
			return;
		}

		pattern = Pattern.compile(MutantConst.PATTERN_TTTT);
		occurrencesAccumulator += Arrays.stream(dna).parallel().filter(pattern.asPredicate()).count();

		if (testStatus()) {
			return;
		}

		pattern = Pattern.compile(MutantConst.PATTERN_GGGG);
		occurrencesAccumulator += Arrays.stream(dna).parallel().filter(pattern.asPredicate()).count();

		if (testStatus()) {
			return;
		}

	}

	private boolean testStatus() {
		if (occurrencesAccumulator >= MutantConst.PATTERNS_COUNT) {
			status.setConditionReached(true);
			return true;
		}
		return false;
	}

	private String[] retrieveColumns(String[] dna) {
		String[] cols = new String[dna.length];
		for (int i = 0; i < dna.length; i++) {
			cols[i] = processColumns(dna, i);
		}
		return cols;
	}

	private String processColumns(String[] dna, int pos) {
		List<Character> lista = Arrays.stream(dna).parallel()
				.map(x -> x.charAt(pos)).collect(Collectors.toList());

		StringBuilder builder = new StringBuilder();
		lista.forEach(ch -> builder.append(ch));
		return builder.toString();
	} 

	private char[][] defineMatrix(String[] dna) {
		int size = dna.length;
		char[][] matrix = new char[size][size];
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				matrix[row][col] = dna[row].charAt(col);
			}
		}
		return matrix;

	}

	private void procesLeftToRightRight(List<Position> positions, char[][] matrix) {
		if (positions.size() == MutantConst.MIN_SEQUENCE_LENGTH) {
			return;
		}
		List<Position> diSubUp = copyPositions(positions.subList(0, positions.size() - 1));
		String anotherRight = retrieveFromMatrix(incrementCol(diSubUp), matrix);
		String[] param = new String[1];
		param[0] = anotherRight;
		patternsChecker(param);
		if (status.isConditionReached())
			return;
		procesLeftToRightRight(diSubUp, matrix);
	}

	private void procesLeftToRightDown(List<Position> positions, char[][] matrix) {
		if (positions.size() == MutantConst.MIN_SEQUENCE_LENGTH) {
			return;
		}
		List<Position> diSubDown = copyPositions(positions.subList(0, positions.size() - 1));
		String anotherDown = retrieveFromMatrix(incrementRow(diSubDown), matrix);
		String[] param = new String[1];
		param[0] = anotherDown;
		patternsChecker(param);
		if (status.isConditionReached())
			return;
		procesLeftToRightDown(diSubDown, matrix);
	}

	private void procesRightToLeftLeft(List<Position> positions, char[][] matrix) {
		if (positions.size() == MutantConst.MIN_SEQUENCE_LENGTH) {
			return;
		}
		List<Position> diSubLeft = copyPositions(positions.subList(0, positions.size() - 1));
		String anotherLeft = retrieveFromMatrix(decrementCol(diSubLeft), matrix);
		String[] param = new String[1];
		param[0] = anotherLeft;
		patternsChecker(param);
		if (status.isConditionReached())
			return;
		procesRightToLeftLeft(diSubLeft, matrix);
	}

	private void procesRightToLeftDown(List<Position> positions, char[][] matrix) {
		if (positions.size() == MutantConst.MIN_SEQUENCE_LENGTH) {
			return;
		}
		List<Position> diSubDown = copyPositions(positions.subList(0, positions.size() - 1));
		String anotherDown = retrieveFromMatrix(incrementRow(diSubDown), matrix);
		String[] param = new String[1];
		param[0] = anotherDown;
		patternsChecker(param);
		if (status.isConditionReached())
			return;
		procesRightToLeftDown(diSubDown, matrix);
	}

	private List<Position> incrementCol(List<Position> positions) {
		positions.stream().parallel().forEach(p -> ((Position) p).incrementCol());
		return positions;
	}

	private List<Position> incrementRow(List<Position> positions) {
		positions.stream().parallel().forEach(p -> ((Position) p).incrementRow());
		return positions;
	}

	private List<Position> decrementCol(List<Position> positions) {
		positions.stream().parallel().forEach(p -> ((Position) p).decrementCol());
		return positions;
	}

	private List<Position> decrementRow(List<Position> positions) {
		positions.stream().parallel().forEach(p -> ((Position) p).decrementRow());
		return positions;
	}

	private String retrieveFromMatrix(List<Position> positions, char[][] matrix) {
		StringBuilder builder = new StringBuilder();
		positions.stream().parallel().forEach(p -> builder.append(matrix[p.getRow()][p.getCol()]));
		return builder.toString();

	}

	private List<Position> copyPositions(List<Position> positions) {
		ArrayList<Position> copiedPositions = new ArrayList<Position>();
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			Position a = it.next();
			Position b = new Position();
			b.setCol(a.getCol());
			b.setRow(a.getRow());
			copiedPositions.add(b);
		}
		return copiedPositions;
	}

	private ArrayList<Position> diagonalLeftToRight(int size) {

		ArrayList<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < size; i++) {
			Position position = new Position();
			position.setCol(i);
			position.setRow(i);
			positions.add(position);
		}
		return positions;
	}

	private ArrayList<Position> diagonalRightToLeft(int size) {
		ArrayList<Position> positions = new ArrayList<Position>();
		int start = size - 1;
		for (int i = 0; i < size; i++) {
			Position position = new Position();
			position.setCol(start - i);
			position.setRow(i);
			positions.add(position);
		}
		return positions;
	}

}
