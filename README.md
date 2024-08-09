Asunciones
----------
- Se almacena la fecha de cada venta, aunque no es un requisito explícito del sistema.
----------
Dependencias
----------
- Requiere 'mockito.core' versión 5.12.0.

----------
Decisiones de diseño y cuestiones de arquitectura
----------
- Arquitectura: Se ha optado por una arquitectura "by feature", agrupando las unidades de trabajo en módulos, uno por cada característica o feature del sistema.
- Modelo de Datos: El modelo de datos se basa en un análisis y diseño previo al desarollo, realizado en un diagrama entidad-relación. Ver anexo 'ER-supermercados' al final del documento.
  Relaciones entre Entidades: En el caso de CadenaSupermercados, se decide usar una lista de supermercados dentro de la cadena en lugar de una tabla intermedia. 
Esta decisión se basa en que la relación es de muchos a uno, simplificando el modelo de datos.
- Control de Versiones: Se ha optado por ramificar los esfuerzos de trabajo para identificar cada una de las iteraciones ágiles realizadas y separar el trabajo por características y por capas. 
- Identificadores Únicos: Se utilizan UUIDs como identificadores. Esta elección se debe a que, al trabajar con estructuras de datos en memoria en lugar de persistentes, 
los UUIDs proporcionan una solución elegante y eficaz para garantizar identificadores únicos.
----------
Optimizaciones
----------
 Si se deseara continuar iterando sobre el producto, se consideran las siguientes optimizaciones:
- Hacer uso de containerization (e.g. Docker)
- Hacer uso de un gestor de dependencias (Maven / Gradle)
- Servicios Auxiliares: Agregar un servicio getAllVentasBySupermercadoId() en VentaRepository para mejorar la eficiencia. Actualmente, 
se realiza la funcionalidad deseada (obtener todas las ventas de un supermercado) mediante "joins" o filtrados entre estructuras de datos completas en memoria, 
lo cual es costoso en términos de tiempo de ejecución. La creación de funcionalidades auxiliares en el DAO puede mejorar significativamente la performance del sistema
en varias de los servicios.

- Supermercado: Horario de Apertura, horario de cierre: Mantener la hora como un entero presenta limitaciones para horarios con minutos (e.g.: 16:15, 16:30), aunque
si se desea dar soporte para horarios de ese estilo, es factible aún modelándolo con Integer.

- Venta: Almacenar los ingresos de la venta en su modelo de datos es una inversión en espacio (64 bits para un Double) por objeto de datos, 
pero resulta en una significativa mejora en eficiencia al reducir la necesidad de consultas adicionales para obtener datos del producto asociado a una venta.

- Generalización de funcionalidades y abstracción: Abstraer valores arbitrarios, como el número 5 para el top 5 de productos más vendidos, 
para generalizar la funcionalidad a un top K de productos y hacerla más flexible.

- Anotaciones throws en interfaces: Para documentar las excepciones que pueden ser lanzadas por los métodos.

- Los ids y las fechas de creación de entidades no deberían traerse desde la capa de servicio, pueden generarse en el repositorio al persistir la información.
- Supermercado y Venta: No se asocia el rango horario de un supermercado a la capacidad de vender, lo que permite realizar ventas incluso cuando un supermercado está cerrado.
----------
Anexo ER-supermercados
  ![ER-supermercados](https://github.com/user-attachments/assets/518e57c7-beb9-47cb-98fe-14e78e8373b8)
