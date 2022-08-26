# vaccineInventory

- clonar proyecto desde el repositorio.

- descargar las dependencias del build.gradle.

- crear la base de datos vaccine-inventory como esta en la configuración del datasource del application.yml,
  cuenta con el db.changelog que contiene las sentencias para crear las tablas de la base de datos y carga información inicial para poder probar los end points.

- el proyecto se despliega en el puerto 8080 como esta en el application.yml.

- las rutas de los end ponit son las siguientes:

  http://localhost:8080/loginUser -> para el inicio de sesión

  http://localhost:8080/saveEmployee -> para guardar y actualizar información de los empleados, este método también crea un nuevo usuario con el fistName.lastName como usuario y el dni como clave

  http://localhost:8080/findAll -> lista todos los empleados que estén activos

  http://localhost:8080/getEmployeeById -> obtiene los empleados por id

  http://localhost:8080/deleteEmployee -> inactiva a los empleados

  http://localhost:8080/employee/search -> devuelve un paginado de los empleados aplicando los filtros de (dni, firstName, lastName, fecha de vacunación y estado de vacunación)

  http://localhost:8080/findByStatus -> devuelve la lista de empleados por estado de vacunación

  http://localhost:8080/saveVaccine -> guarda los datos de la vacuna de un empleado

Hecho por Pedro Arauz
