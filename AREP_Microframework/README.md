# 🚀 Microframework AREP

Este proyecto implementa un **microframework web en Java** que permite servir archivos estáticos (HTML, CSS, JS) y definir servicios REST mediante funciones lambda.

---

## 📖 Marco Teórico

### ¿Qué es un Framework?
Un framework es una estructura de software que provee herramientas, librerías y convenciones para facilitar el desarrollo de aplicaciones.  
A diferencia de una librería, un framework establece un esqueleto general sobre el cual los desarrolladores añaden su propia lógica.  

El framework controla el flujo de ejecución principal (Inversión de Control) y los desarrolladores definen únicamente las partes específicas de la aplicación, lo que permite:

- Reutilizar componentes.
- Reducir el código repetitivo.
- Estandarizar el desarrollo.
- Aumentar la productividad.

En el caso de los frameworks web, estos abstraen los detalles de bajo nivel de los protocolos de red (como HTTP) y facilitan la creación de rutas REST, manejo de peticiones y servido de archivos estáticos.

---

## 🛠️ Arquitectura del Proyecto

Este microframework está construido sobre **Java Sockets** y provee una API simple con 3 funciones principales:

- `get(path, handler)` → Definir servicios REST con lambdas.  
- `staticfiles(folder)` → Especificar la carpeta de archivos estáticos.  
- `start()` → Iniciar el servidor web.  

---

## 📂 Explicación de Clases del Microframework

### `App.java`
- Es el **punto de entrada** de la aplicación.  
- Aquí el desarrollador define:  
  - La carpeta de archivos estáticos con `staticfiles()`.  
  - Las rutas REST con `get(path, handler)`.  
  - Finalmente inicia el servidor con `start()`.  
- Representa un ejemplo de cómo un usuario puede construir su aplicación sobre el framework.

---

### `MiniSpark.java`
- Es el **núcleo del framework**.  
- Se encarga de:  
  - Abrir el servidor en el puerto 8080 usando `ServerSocket`.  
  - Escuchar y procesar peticiones HTTP.  
  - Buscar si la ruta solicitada coincide con alguna definida con `get()`.  
  - Servir archivos estáticos desde la carpeta configurada con `staticfiles()`.  
  - Construir la respuesta HTTP con headers y contenido.  
- Es la clase que implementa la **lógica principal del servidor**.

---

### `Request.java`
- Representa la **petición HTTP** recibida del cliente.  
- Almacena:  
  - Método de la petición (`GET`, `POST`, etc.).  
  - Ruta solicitada (`/hello`, `/pi`).  
  - Parámetros de la query string (`?name=Pedro`).  
  - Cuerpo de la petición (en caso de `POST` o `PUT`).  
- Proporciona métodos para acceder fácilmente a los valores enviados en la petición.

---

### `Response.java`
- Representa la **respuesta HTTP** que el servidor enviará al cliente.  
- Permite definir:  
  - El tipo de contenido (`Content-Type`, como `text/html`, `application/json`).  
  - El cuerpo de la respuesta (texto o datos que se envían al navegador).  
- Facilita la construcción de respuestas personalizadas dentro de los handlers REST.

---

### `Route.java`
- Es una **interfaz funcional**.  
- Define la forma en que deben implementarse los handlers de rutas REST.  
- Permite que se usen **expresiones lambda** para responder a las peticiones, por ejemplo:  
  get("/hello", (req, res) -> "Hello " + req.getValues("name"));

---

## 📸 Capturas de pantalla
Si abrimos el link http://localhost:8080/hello?name=Pedro encontramos lo siguiente:  
<img width="357" alt="image" src="https://github.com/user-attachments/assets/d87b7fc4-f869-4a5c-8cba-dc9b5ce51851" />  
Si cambiamos el nombre por Nicolas:  
<img width="357" alt="image" src="https://github.com/user-attachments/assets/331cfd46-c3e7-4303-88ab-c259cb7617ee" />  
Ahora ingresamos a http://localhost:8080/pi:  
<img width="234" alt="image" src="https://github.com/user-attachments/assets/bfb362bf-0a21-4896-bfad-6f1e4ef5b998" />  
Y si ingresamos al archivo de index, encontramos lo siguiente:  
<img width="787" alt="image" src="https://github.com/user-attachments/assets/09b94561-9752-4ede-bac7-1c44cffc4ef9" />
