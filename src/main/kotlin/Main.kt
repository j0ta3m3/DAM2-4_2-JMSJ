fun main(args: Array<String>) {
    var producto1 = Producto("Patatas", 1.3)
    var producto2 = Producto("Colacao", 1.4)

    var productosLista1 = listOf(Producto("Papas", 1.7), producto2, producto1)
    var productosLista2 = listOf(Producto("tomate", 1.7), producto1)
    var pedido1 = Pedido(productosLista1, true)
    var pedido2 = Pedido(productosLista2, false)
    var pedidosLista1 = listOf(Pedido(productosLista1, true), pedido1, pedido2)
    var ciudad1 = Ciudad("Jerez")
    var ciudad2 = Ciudad("Malaga")
    var cliente1 = Clientes("Carlos", ciudad1, pedidosLista1)
    var cliente2 = Clientes("Pedro", ciudad2, pedidosLista1)
    var cliente3 = Clientes("Pako", ciudad1, pedidosLista1)
    var clientesLista = listOf<Clientes>(cliente1, cliente3)
    var tienda1 = Tienda("Alameda", clientesLista)

}

data class Tienda(val nombre: String, val clientes: List<Clientes>) {

    fun obtenerConjuntoDeClientes(): Set<Clientes> {
        return clientes.toSet()
    }

    fun obtenerCiudadesDeClientes(): Set<Ciudad> {
        return clientes.map { it.ciudad }.toSet()
    }

    fun obtenerClientesPor(ciudad: Ciudad): List<Clientes> {
        return clientes.filter { it.ciudad == ciudad }.toList()
    }
//4.2.3 Implementa los siguientes métodos, recomendado haciendo uso de all, any, count y find. Piensa cuál usar en los siguientes métodos:

    fun checkTodosClientesSonDe(ciudad: Ciudad): Boolean {

        return clientes.all { ciudad == it.ciudad }
    }

    fun hayClientesDe(ciudad: Ciudad): Boolean {

        return clientes.any { ciudad == it.ciudad }

    }

    fun cuentaClientesDe(ciudad: Ciudad): Int {

        return clientes.count { it.ciudad == ciudad }
    }

    fun encuentraClienteDe(ciudad: Ciudad): Clientes? {
        return clientes.find { it.ciudad == ciudad }
    }

// 4.2.4 Implementa un método para que devuelva una lista de clientes, ordenadas descendientemente por el número de pedidos que ellos han realizado. Puedes hacer uso de sortedDescending o sortedByDescending.

    fun obtenerClientesOrdenadosPorPedidos(): List<Clientes> {
        return clientes.sortedByDescending { it.pedidos.count() }
    }

    /* 4.2.5 - Implementa un método para que devuelva una lista de clientes que tiene más pedidos sin entregar que entregados,
    puedes hacer uso de Partition. */
    fun obtenerClientesConPedidosSinEngregar(): Set<Clientes> =
        clientes.partition { it.pedidos.count { it.estaEntregado } > it.pedidos.count { !it.estaEntregado } }.second.toSet()

    // 4.2.6 (2) - Los productos que fueron pedidos por al menos un cliente. //Revisar (es correcto el un cliente??)
    fun obtenerProductosPedidos(): Set<Producto> =
        clientes.flatMap { it.pedidos }.filter { it.estaEntregado }.flatMap { it.productos }.toSet()

    /* 4.2.8 (2) - Cuenta la cantidad de veces que un producto se ha pedido.Ten en cuenta que un cliente puede
    pedir el pedido varias veces. Puedes usar Cliente.obtenerProductosPedidos() Verificar si el filter de repetido es correcto*/
    fun obtenerNumeroVecesProductoPedido(producto: Producto): Int =
        obtenerProductosPedidos().filter { !clientes.contains(it.nombre)/*it.nombre != it.nombre in clientes*/ }
            .count { producto in obtenerProductosPedidos() }

    // 4.2.9 - Implementa un método para que un map que almacene los clientes viviendo en una determinada ciudad. Puedes usar groupBy
    fun agrupaClientesPorCiudad(): Map<Ciudad, List<Clientes>> = clientes.groupBy { it.ciudad }

    // AMPLIACIÓN

    /* 4.2.7 - Implementa un método para que devuelva el conjunto de productos que han sido pedidos por TODOS los clientes.
       Puedes usar flatMap y fold y Puedes usar Cliente.obtenerProductosPedidos()*/
    fun obtenerProductosPedidosPorTodos(): Set<Producto> = //SIN TERMINAR
        clientes.flatMap { it.pedidos }.flatMap { it.productos }.toSet() //.all { it.estaEntregado }

    // 4.2.10 - Implementa un método para que devuelva un mapa desde el nombre del cliente al cliente, puedes usar associateBy
    fun mapeaNombreACliente(): Map<String, Clientes> = clientes.associateBy { it.nombre }

    // 4.2.10 (2) - Un mapa desde el cliente a su ciudad, puedes usar associateWith
    fun mapeaClienteACiudad(): Map<Clientes, Ciudad> = clientes.associateWith { it.ciudad }

    // 4.2.10 (3) - Un mapa desde el nombre del cliente a su ciudad, puedes usar associate // SIN HACER
    // fun mapeaNombreClienteACiudad(): Map<String, Ciudad> = clientes.associate { }

    // 4.2.11 - Implementa un método para que devuelva el cliente que ha realizado más pedidos en la tienda, puedes usar maxByOrNull.
    fun obtenerClientesConMaxPedidos(): Clientes? = clientes.maxByOrNull { it.pedidos.count() }


}

data class Clientes(val nombre: String, val ciudad: Ciudad, val pedidos: List<Pedido>) {
    override fun toString() = "$nombre from ${ciudad.nombre}"

    // 4.2.6 - Los productos pedidos por un cliente, pudiendo usar flatmap
    fun obtenerProductosPedidos(): List<Producto> = pedidos.flatMap { it.productos }


    // 4.2.8 - Encuentra el producto más caro entre los productos entregados pedidos por el cliente. Usa Pedido.estaEntregado
    fun encuentraProductoMasCaro(): Producto? =
        pedidos.filter { it.estaEntregado }.flatMap { it.productos }.maxByOrNull { it.precio }
    //find { it.precio == maxOf(it.precio) } // REVISAR

    // 4.2.12 - La suma de los precios de todos los productos pedidos por un cliente, puedes usar flatMap y sumOf
    fun dineroGastado(): Double = pedidos.filter { it.estaEntregado }.flatMap { it.productos }.sumOf { it.precio }
    //fun dineroGastado(): Double = clientes.flatMap { it.pedidos }.flatMap { it.productos }.sumOf { it.precio }

    // AMPLIACIÓN

    // 4.2.11 (2) - El producto más caro que ha pedido el cliente, puedes usar flapMap y maxByOrNull y callable references.
    fun obtenerProductoMasCaroPedido(): Producto? = pedidos.flatMap { it.productos }.maxByOrNull { it.precio }
}

data class Pedido(val productos: List<Producto>, val estaEntregado: Boolean)

data class Producto(val nombre: String, val precio: Double) {
    override fun toString() = "'$nombre' for $precio"
}

data class Ciudad(val nombre: String) {
    override fun toString() = nombre
}