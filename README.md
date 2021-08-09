## PruebaML "Mutantes" exámen Mercadolibre.

- Tabla de contenidos
	- Letra del problema
	- Análisis
	- Desarrollo
	- Publicación de servicios	
	- Como ejecutar la aplicación
	- Code Coverage
	- Vista General del sistema
	- Vista General del sistema	
	- Tests de carga
	- Conclusionesa
<br/><br/>
#### Letra del problema 

![imagen](./images/letra1.jpg)
![imagen](./images/letra2.jpg)


#### Análisis

El ejercicio se centra en poder realizar la búsqueda de un mínimo de 2 ocurrencias de 4 letras iguales a la que llamaremos “patrón” o “patrones” que provienen de 4 diferentes tipos de letras (A,C,T,G), se pueden considerar entonces 4 posibles patrones (AAAA,CCCC,TTTT,GGGG).<br/>Se menciona que la matriz objetivo es de (N x N) por lo que en principio nuestra consideración es que puede crecer de dimensión siempre teniendo tantas filas como columnas. <br/>También relata que la dirección en que se pueden ubicar estos patrones es de forma horizontal, vertical y oblicua de modo que se deben recorrer las columnas, las filas y diagonales. <br/>Para la realización del algoritmo de búsqueda primero estudiamos las tecnologías a utilizar, en nuestro caso particular hemos tomado las siguientes decisiones:<br/><br/>
##### Lenguaje de desarrollo: 
Java versión 1.8 (Permite la programación funcional, implementa las características de lambdas y streams que hacen posibles operaciones masivas con tiempos de ejecución más veloces y con menos líneas de código)<br/><br/>
##### Arquitectura: 
API REST desplegada en contenedor web y persistencia en BBDD no SQL.<br/><br/>
##### Sistema operativo: 
Windows Server 2012R2  (X64) – 2CPU Virtuales - 4Gb de RAM<br/><br/>
##### Servidor Web: 
[Apache Tomcat versión 8.5.69](https://tomcat.apache.org/download-80.cgi) <br/><br/>
##### Base de datos: 
[MongoDB 3.4](https://www.mongodb.com/try/download/community) (No es la última pero si la que soporta el server Windows Server 2012R2)<br/><br/>
##### IDE de desarrollo: 
Eclipse 2019-12.<br/><br/>
##### Herramientas: 
Postman Canary, JMeter, RESTful Stress, Enterprise Architect.<br/><br/>
##### Frameworks: 
Spring Boot, Apache Maven, JUnit, Surefire, Jacoco.<br/><br/>

#### Desarrollo
En una primera instancia se desarrolló el algoritmo teniendo en cuenta las siguientes consideraciones:<br/>
Dos instancias de patrones ya son suficientes para considerar a un candidato como “mutante” motivo por el cual en ese momento (si es que llega a dar la condición) abandonamos el recorrido para evitarnos el consumo del resto del algorítmo.<br/>
Generar la matríz es lo más costoso por lo que evitaremos este proceso mientras no sea absolutamente necesario.<br/>
Comenzaremos procesando el flujo de entrada tal como viene ya que este array son las filas donde ya tengo posibilidad de encontrar nuestros 2 patrones, las operaciones de búsqueda en casi todos los casos se realizan con streams con operaciones de filtrado, pattern match y collect.<br/>
El algoritmo va acumulando los matches de modo que si no hemos llegado a 2 ocurrencias con las filas proseguimos con las columnas, en este caso se realiza una operación de recupero y nuevamente hacemos un bulk processing con Streams en búsqueda de patrones.<br/>
Si para esta instancia no se han tenido ocurrencias se procede a generar la matríz para buscar las diagonales.<br/>
En el caso de las diagonales el algoritmo tiene en cuenta que el largo mínimo de una diagonal para poder obtener un match es de 4 caractéres motivo por el cual va a procesar desde las diagonales de mayor largo, recorriendo las derivadas hasta que sean de ese largo y abandona.<br/>
Otro aspecto que se tiene en cuenta es los casos en que la matríz tenga dimensiones iguales o mayores a 8 de lado en los que es posible más de 1 patrón igual por línea analizada.<br/> 
Para estos casos se activa una búsqueda extendida que permite, si se ha encontrado un patrón, se pueda analizar desde esa posición hacia adelante por una nueva ocurrencia ya que las búsquedas por expresiones regulares o las propias de la clase String no reconocen mas de 1 ocurrencia si se encuentran de forma adyacente, es decir uno a continuación del otro.<br/>
Para la persistencia el uso de MongoDB nos permite una fácil configuración, alto tiempo de respuesta y está disponible cross platform por lo que es muy versátil, nos valemos de la opción que provee el motor de utilizar el guardado en modo ```"Upsert"``` el cual realiza un update en caso de existir el registro o un insert en caso de no existir, validando automáticamente la no duplicidad de registros y cumpliendo de este modo con el requisito de almacenar solo 1 regístro por individuo.<br/>
MongoDB nos pide para tal fin que el registro tenga un id el cual generamos a partir de un Hash hexadecimal que es único por individuo a testear ya que se genera a partir del código del ADN evitando que si hay múltiples request de una misma secuencia estas sean almacenadas, se evaluó guardar los hashes de los request ya consultados en un HashTable para evitar la consulta al algorítmo pero se descartó en principio ya que sería necesario tener en memoria tantos hashes como objetos en la bbdd, quizás en un futuro se pueda avanzar en crear una caché de Hashes en otro componente externo o servicio que se pueda consultar antes de avanzar hacia el algorítmo.<br/><br/>

#### Publicación de servicios
- [Endpoint mutante](http://35.199.98.123:8080/ml/mutant/).
- [Endpoint estadísticas](http://35.199.98.123:8080/ml/stats)

#### Como ejecutar la aplicación
Descargar la herramienta Postman Canary desde este [link](https://www.postman.com/downloads/canary/).<br/><br/>
Ir a la opción new --> HttpRequest.<br/>
1) Seleccionar Method ("POST" para /mutant/ y "GET" para /stats)<br/>
2) Cargar Endpoint<br/>
3) Seleccionar tab Body<br/>
4) Seleccionar formato Json<br/>
5) Cargar mensaje en formato Json<br/>
6) Enviar<br/>

![imagen](./images/postmanCarga.jpg)
<br/><br/>
Con respuesta 200 OK cuando es humano<br/>
![imagen](./images/respuesta-humano.jpg)

Con respuesta 403 Forbidden cuando es mutante<br/>
![imagen](./images/respuesta-mutant.jpg)

Para el servicio /stats se utiliza "GET" y el servicio responde con un Json <br/>
![imagen](./images/respuesta-stats.jpg)




#### Code Coverage
Se utilizaron 2 Herramientas de de code coverage, por un lado el plugin de JUnit para ir ejecutando y consultando en tiempo de desarrollo la cobertura de cada Test y por otro lado se adjunta el plugin Jacoco que está incluído en el pom del proyecto de modo que quien desee puede correrlo sin necesidad de un IDE de desarrollo, solo necesita tener instalado Maven y el servicio de MongoDB levantado (en nuestro caso la instalación utiliza los datos default de puerto 27017 y se agregaron los siguientes datos: usr:”admin”, pwd:”admin” y la bbdd:”mutants”, de todas formas es configurable desde el archivo src/main/resources/application.properties).<br/>
Ya hay en la carpeta documents una sub carpeta Jacoco que se puede descargar y posee el reporte en formato html para poder hacer drill down por componente.<br/>
Para generar el reporte, ejecutar en el path raíz del proyecto donde se ubica el archivo pom el siguiente comando:<br/><br/>
```"mvn clean test jacoco:report" ```  <br/> <br/>
El reporte se generará en la carpeta target/site/Jacoco
Tener en cuenta que para ejecutar el reporte se debe cambiar dentro del pom en la siguiente entrada de Surefire
la opción ``` “<skipTests>true</skipTests>” por “<skipTests>false</skipTests>”``` de modo que quede así:
<br/>
```
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
	<skipTests>false</skipTests>
  </configuration>
</plugin>
```
<br/>
Los resultados de las corridas se adjuntan en las siguientes imágenes:<br/>

###### JUnit:

![imagen](./images/JUnitCoverage.jpg)

###### Jacoco:

![imagen](./images/jacoco.jpg)

#### Vista General del sistema

##### Diagrama de clases

![imagen](./images/DiagramaGeneral.jpg)

##### Validadores y Procesador

![imagen](./images/Validators_Processors.jpg)



#### Tests de carga
Con la herramienta RESTful Stress se hizo una prueba de 10000 (diez mil iteraciones) con ADN no Mutante el cual es nuestro peor escenario ya que recorre todo el algorítmo, obteniendo un tiempo promedio de respuesta de 5 milisegundos, al igual que para el servicio "stats" como se aprecia a continuación:<br/><br/>
##### Input
![imagen](./images/input-RESTStress.jpg)
<br/>
##### Estadísticas /mutant/
![imagen](./images/RESTStress-Stats.jpg)
<br/>
##### Persistencia único regístro
![imagen](./images/mongo-Unique.jpg)
<br/>
##### Estadísticas stats/
![imagen](./images/RESTStress-Stats2.jpg)
<br/>
#### Conclusiones
El servicio trabaja sobre un container, teniendo en cuenta las apreciaciones de la letra que plantea concurrencia de request masivos deberíamos hacer clustering del servicio administrado por un load balancer de modo de escalar de forma horizontal el servicio y si llegado el caso el cuello de botella se presenta sobre la base de datos se puede hacer un "Sharding" o partición sobre la misma para administrar las peticiones de modo diferenciado.

