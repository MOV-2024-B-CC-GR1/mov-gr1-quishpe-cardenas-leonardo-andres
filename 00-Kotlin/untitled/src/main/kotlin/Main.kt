package org.example

fun main() {
    println("Hello World!")
    otraFuncionAdentro()
    imprimirNombre("Juan")

    calcularSueldo(10.00) //solo parametro requerido
    calcularSueldo(10.00,15.00,20.00) //parametro requerido y sobreescribir parametros opcionales

    // Named parameters
    // calcularSueldo(sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00) //usando le parametro bonoespecial en 2da posicion
                                                       //gracias a los parametros nombrados
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00);

    //usando el parametro bonoespecial en 1era posicion
    //usando el parametro sueldo en 2do
    //gracias a los parametros nombrados

    Public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico Implicito"


    init{
        this.numeroUno
        this.numeroDos
        numeroUno
        numeroDos
        this.soyPublicoExplicito
        soyPublicoImplicito
    }

    constructor( //constructor secundario
        uno: Int?, // entero nullable
        dos: Int,
    ):this(
        if(uno == null) 0 else uno,
        dos
    ){
        //Bloque de codigo de constructor secunadrio
    }

    constructor( //constructor secundario
        uno: Int,
        dos: Int?, // entero nullable
    ):this(
        uno
        if(dos == null) 0 else dos,
        )

    constructor( //constructor secundario
        uno: Int?,  // entero nullable
        dos: Int?,  // entero nullable
    ):this(
        if(uno == null) 0 else uno,
        if(dos == null) 0 else dos
    )

}

fun otraFuncionAdentro(){
    println("otraFuncionAdentro")
}

fun imprimirNombre(nombre:String): Unit{
    fun otraFuncionAdentro(){
        println("otraFuncionAdentro")
    }
    println("Nombre: ${nombre}")
}

fun calcularSueldo(
    sueldo:Double,
    tasa:Double = 12.8,
    bonoEspecial:Double? = null
): Double{
    //int -> Int? (nullable)
    //string -> string? (nullable)
    //date -> date? (nullable)
    if(bonoEspecial==null){
        return sueldo * (100/tasa)
    } else {
        return sueldo * (100/tasa) * bonoEspecial
    }
}

//clase con SINTAXIS KOTLIN


//clase con SINTAXIS JAVA

abstract class NumerosJava{
    protected val numeroUno:Int,
    private val numeroDos:Int,
}

fun sumar(): Int{
    val total = numeroUno + numeroDos
    agregarHistorial(total)
    return total
}

companion object 
