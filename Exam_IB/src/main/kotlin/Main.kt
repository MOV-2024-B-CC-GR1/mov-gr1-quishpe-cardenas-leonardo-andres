import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.Auto
import model.ParteAuto
import java.io.File
import java.util.*

// Directorio donde se guardarán los archivos
const val BASE_PATH = "C:/Users/Leo/Documents/movilez/mov-gr1-quishpe-cardenas-leonardo-andres/mov-gr1-quishpe-cardenas-leonardo-andres/Exam_IB"

// Archivos para guardar datos
val AUTOS_FILE = File("$BASE_PATH/autos.json")
val PARTES_FILE = File("$BASE_PATH/partes.json")

var autos = mutableListOf<Auto>()
var partes = mutableMapOf<Int, MutableList<ParteAuto>>() // Relación Auto (id) -> Partes

fun main() {
    cargarDatos() // Cargar datos al inicio

    val scanner = Scanner(System.`in`)
    while (true) {
        println("\n===== Menú Principal =====")
        println("1. Crear Auto")
        println("2. Leer Autos")
        println("3. Actualizar Auto")
        println("4. Eliminar Auto")
        println("5. Crear Parte de Auto")
        println("6. Leer Partes de un Auto")
        println("7. Salir")
        print("Elige una opción: ")

        try {
            when (scanner.nextInt()) {
                1 -> crearAuto(scanner)
                2 -> leerAutos()
                3 -> actualizarAuto(scanner)
                4 -> eliminarAuto(scanner)
                5 -> crearParteAuto(scanner)
                6 -> leerPartesAuto(scanner)
                7 -> {
                    guardarDatos() // Guardar datos al salir
                    println("Saliendo del programa...")
                    return
                }
                else -> println("Opción inválida. Inténtalo de nuevo.")
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número válido.")
            scanner.nextLine() // Limpiar el buffer del scanner
        }
    }
}

fun guardarDatos() {
    val json = Json { prettyPrint = true }

    // Crear directorio si no existe
    val directory = File(BASE_PATH)
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Guardar datos de autos
    AUTOS_FILE.writeText(json.encodeToString(autos))

    // Guardar datos de partes
    PARTES_FILE.writeText(json.encodeToString(partes))

    println("Datos guardados correctamente en $BASE_PATH.")
}

fun cargarDatos() {
    val json = Json { ignoreUnknownKeys = true }

    // Crear directorio si no existe
    val directory = File(BASE_PATH)
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Cargar autos
    if (AUTOS_FILE.exists() && AUTOS_FILE.length() > 0) {
        try {
            val autosJson = AUTOS_FILE.readText()
            autos = json.decodeFromString(autosJson)
        } catch (e: SerializationException) {
            println("Archivo autos.json corrupto. Inicializando datos vacíos.")
            autos = mutableListOf()
            guardarDatos() // Reescribe con datos iniciales
        }
    } else {
        autos = mutableListOf()
        guardarDatos() // Crear archivo vacío
    }

    // Cargar partes
    if (PARTES_FILE.exists() && PARTES_FILE.length() > 0) {
        try {
            val partesJson = PARTES_FILE.readText()
            val partesMap = json.decodeFromString<Map<Int, MutableList<ParteAuto>>>(partesJson)
            partes.putAll(partesMap)
        } catch (e: SerializationException) {
            println("Archivo partes.json corrupto. Inicializando datos vacíos.")
            partes = mutableMapOf()
            guardarDatos() // Reescribe con datos iniciales
        }
    } else {
        partes = mutableMapOf()
        guardarDatos() // Crear archivo vacío
    }

    println("Datos cargados desde $BASE_PATH.")
}

fun crearAuto(scanner: Scanner) {
    println("\n=== Crear Auto ===")
    print("Nombre del Auto: ")
    val nombre = scanner.next()

    var fecha: String
    while (true) {
        print("Fecha de fabricación (yyyy-MM-dd): ")
        fecha = scanner.next()
        if (fecha.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            break
        } else {
            println("Formato inválido. Por favor, ingresa la fecha en formato yyyy-MM-dd.")
        }
    }

    var electrico: Boolean
    while (true) {
        print("¿Es eléctrico? (true/false): ")
        try {
            electrico = scanner.nextBoolean()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa true o false.")
            scanner.nextLine() // Limpiar el buffer
        }
    }

    var precio: Double
    while (true) {
        print("Precio: ")
        try {
            precio = scanner.nextDouble()
            if (precio < 0) {
                println("El precio no puede ser negativo.")
            } else {
                break
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número decimal.")
            scanner.nextLine()
        }
    }

    val id = if (autos.isEmpty()) 1 else autos.last().id + 1
    autos.add(Auto(id, nombre, fecha, electrico, precio))
    println("Auto creado con éxito: $id - $nombre")
}

fun leerAutos() {
    println("\n=== Lista de Autos ===")
    if (autos.isEmpty()) {
        println("No hay autos registrados.")
    } else {
        autos.forEach {
            println("ID: ${it.id}, Nombre: ${it.nombre}, Fecha: ${it.fechaFabricacion}, Eléctrico: ${it.esElectrico}, Precio: ${it.precio}")
        }
    }
}

fun actualizarAuto(scanner: Scanner) {
    println("\n=== Actualizar Auto ===")
    var id: Int
    while (true) {
        print("ID del Auto a actualizar: ")
        try {
            id = scanner.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número entero.")
            scanner.nextLine()
        }
    }
    val auto = autos.find { it.id == id }
    if (auto == null) {
        println("No se encontró un auto con ese ID.")
        return
    }
    print("Nuevo nombre (${auto.nombre}): ")
    auto.nombre = scanner.next()

    while (true) {
        print("Nueva fecha de fabricación (${auto.fechaFabricacion}): ")
        val fecha = scanner.next()
        if (fecha.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            auto.fechaFabricacion = fecha
            break
        } else {
            println("Formato inválido. Por favor, ingresa la fecha en formato yyyy-MM-dd.")
        }
    }

    while (true) {
        print("¿Es eléctrico? (${auto.esElectrico}): ")
        try {
            auto.esElectrico = scanner.nextBoolean()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa true o false.")
            scanner.nextLine()
        }
    }

    while (true) {
        print("Nuevo precio (${auto.precio}): ")
        try {
            auto.precio = scanner.nextDouble()
            if (auto.precio < 0) {
                println("El precio no puede ser negativo.")
            } else {
                break
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número decimal.")
            scanner.nextLine()
        }
    }
    println("Auto actualizado con éxito.")
}

fun eliminarAuto(scanner: Scanner) {
    println("\n=== Eliminar Auto ===")
    var id: Int
    while (true) {
        print("ID del Auto a eliminar: ")
        try {
            id = scanner.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número entero.")
            scanner.nextLine()
        }
    }
    if (autos.removeIf { it.id == id }) {
        partes.remove(id)
        println("Auto eliminado con éxito.")
    } else {
        println("No se encontró un auto con ese ID.")
    }
}

fun crearParteAuto(scanner: Scanner) {
    println("\n=== Crear Parte de Auto ===")
    var autoId: Int
    while (true) {
        print("ID del Auto: ")
        try {
            autoId = scanner.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número entero.")
            scanner.nextLine()
        }
    }
    if (autos.none { it.id == autoId }) {
        println("No se encontró un auto con ese ID.")
        return
    }

    print("Nombre de la parte: ")
    val nombreParte = scanner.next()

    var cantidad: Int
    while (true) {
        print("Cantidad: ")
        try {
            cantidad = scanner.nextInt()
            if (cantidad < 0) {
                println("La cantidad no puede ser negativa.")
            } else {
                break
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número entero.")
            scanner.nextLine()
        }
    }

    var precio: Double
    while (true) {
        print("Precio por unidad: ")
        try {
            precio = scanner.nextDouble()
            if (precio < 0) {
                println("El precio no puede ser negativo.")
            } else {
                break
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número decimal.")
            scanner.nextLine()
        }
    }

    var fechaReemplazo: String
    while (true) {
        print("Fecha de reemplazo (yyyy-MM-dd): ")
        fechaReemplazo = scanner.next()
        if (fechaReemplazo.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            break
        } else {
            println("Formato inválido. Por favor, ingresa la fecha en formato yyyy-MM-dd.")
        }
    }

    val idParte = if (partes[autoId].isNullOrEmpty()) 1 else partes[autoId]!!.last().id + 1
    partes.computeIfAbsent(autoId) { mutableListOf() }.add(ParteAuto(idParte, nombreParte, cantidad, precio, fechaReemplazo))
    println("Parte creada con éxito.")
}

fun leerPartesAuto(scanner: Scanner) {
    println("\n=== Leer Partes de un Auto ===")
    var autoId: Int
    while (true) {
        print("ID del Auto: ")
        try {
            autoId = scanner.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Por favor, ingresa un número entero.")
            scanner.nextLine()
        }
    }
    val listaPartes = partes[autoId]
    if (listaPartes.isNullOrEmpty()) {
        println("No se encontraron partes para este auto.")
    } else {
        listaPartes.forEach {
            println("ID Parte: ${it.id}, Nombre: ${it.nombreParte}, Cantidad: ${it.cantidad}, Precio: ${it.precio}, Fecha Reemplazo: ${it.fechaReemplazo}")
        }
    }
}