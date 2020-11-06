import swingRAD.*
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.table.DefaultTableModel
import kotlin.arrayOf as arrayOf1

class Window: JFrame() {
    private val tfValorVuelta = JTextField()
    private val tfCantidadMonedas = JTextField()
    private var aTfValores = arrayOf1<JTextField?>()
    private val pContenedor = JPanel()
    private val pDerecho = JPanel()
    private val sPDerecho = JScrollPane(
        pDerecho,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    )
    private val lResultado = JLabel()

    init{
        panelIzquierdo()
        panelDerecho()

        setMainBar("cambio de monedas")
        setBackground("resources/background/backgroundBlack1.jpg")
        setProperties()
    }

    private fun panelIzquierdo() {

        val pIzquierdo = JPanel()
        pIzquierdo.setProperties(28, 57, 410, 444)
        add(pIzquierdo)

        val taTexto = JTextArea()
        taTexto.setProperties(
            30, 40, 370, 110, false, text = "si i == 1 y j < valor_i entonces c[i,j] es ∞\n" +
                    "sino si i == 1 entonces c[i,j] = 1 + c[1, j-valor_1]\n" +
                    "sino si j < valor_i entonces c[i,j] = c[i-1, j]\n" +
                    "sino c[i,j] = mínimo ( c[i-1, j], 1 + c[i, j-valor_i] )", background = null, border = null
        )
        pIzquierdo.add(taTexto)

        val lValorVuelta = JLabel()
        lValorVuelta.setProperties(70, 150, 150, 28, "Valor de las vueltas")
        pIzquierdo.add(lValorVuelta)

        tfValorVuelta.setProperties(280, 152, 80, 26)
        pIzquierdo.add(tfValorVuelta)

        pIzquierdo.add(lResultado)

        val lCantidadMonedas = JLabel()
        lCantidadMonedas.setProperties(70, 180, 170, 28, "Número de monedas")
        pIzquierdo.add(lCantidadMonedas)

        tfCantidadMonedas.setProperties(280, 182, 80, 26)
        pIzquierdo.add(tfCantidadMonedas)

        val bAbrirCampos = JButton()
        bAbrirCampos.setProperties(260, 220, 100, 32, "Abrir campos")
        bAbrirCampos.addActionListener { abrirCampos() }
        pIzquierdo.add(bAbrirCampos)

        val sValores = JScrollPane(pContenedor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        sValores.setProperties(30, 220, 220, 150)
        pIzquierdo.add(sValores)

        val bCambio = JButton()
        bCambio.addActionListener { cambio() }
        bCambio.setProperties(260, 257, 100, 32, "Cambio")
        pIzquierdo.add(bCambio)

    }

    private fun panelDerecho() {

        pContenedor.setProperties(0, 0, 200, 130, border = null)
        pContenedor.preferredSize = Dimension(200, 130)

        pDerecho.setProperties(0, 0, 767, 630, border = transparentBorder, background = transparent)

        sPDerecho.setProperties(478, 57, 767, 630, background = transparent, border = transparentBorder)
        add(sPDerecho)

    }

    private fun abrirCampos(){
        pContenedor.removeAll()
        pContenedor.setBounds(0, 0, 20, 40 + 32 * tfCantidadMonedas.text.toInt())
        pContenedor.preferredSize = Dimension(200, 40 + 32 * tfCantidadMonedas.text.toInt())

        aTfValores = Array(tfCantidadMonedas.text.toInt()){null}
        for(i in 0 until tfCantidadMonedas.text.toInt()) {
            val tfValori = JTextField()
            tfValori.setProperties(110, 20 + 32 * i, 80, 26)
            aTfValores[i] = tfValori
            pContenedor.add(aTfValores[i])

            val lValor = JLabel()
            lValor.setProperties(20, 18 + 32 * i, 100, 32, "Valor ${i + 1}")
            pContenedor.add(lValor)
        }

        repaint()
    }

    private fun cambio(){
        val valorVuelta = tfValorVuelta.text.toInt()
        val cantidadMonedas = tfCantidadMonedas.text.toInt()

        sPDerecho.setProperties(478, 57, 767, 630)

        pDerecho.background = semiDarkGrayBlue
        pDerecho.setBounds(478, 57, (valorVuelta + 3) * 70, (cantidadMonedas + 2) * 40)
        pDerecho.preferredSize = Dimension((valorVuelta + 3) * 70, (cantidadMonedas + 2) * 40)

        //ingreso los valores al algoritmo
        val datos = IntArray(aTfValores.size){aTfValores[it]!!.text.toInt()}
        datos.sort()
        val algoritmo = Algoritmo(valorVuelta, datos)

        //diseño de la tabla
        val placeholdes = arrayListOf("Valores")
        for(i in 0 .. valorVuelta)
            placeholdes.add(i.toString())
        placeholdes.add("Fila")
        val cabecera: Array<String> = placeholdes.toArray(arrayOfNulls<String>(0))

        val modelo = DefaultTableModel()
        modelo.setColumnIdentifiers(cabecera)
        for (i in 0 .. cantidadMonedas) {
            modelo.addRow(arrayOf1<Any>(""))
            if(i>0)
                modelo.setValueAt(datos[i-1], i, 0)
            for(j in 0 .. valorVuelta){
                modelo.setValueAt(
                    if(algoritmo.matriz[i][j] > 100)
                        "∞"
                    else algoritmo.matriz[i][j].toInt(), i, j+1
                )
            }
            modelo.setValueAt(i, i, valorVuelta+2)
        }

        val tabla = JTable()
        tabla.setProperties(modelo)
        tabla.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                JOptionPane.showMessageDialog(null, calcularComposicion(algoritmo.composicion[tabla.selectedRow][tabla.selectedColumn-1],
                modelo.getValueAt(tabla.selectedRow, tabla.selectedColumn).toString()))
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}
        })

        val header = tabla.tableHeader
        header.preferredSize = Dimension(580, 30)
        header.defaultRenderer = getCustomTable(semiDarkGray2, null, null, white, fontText)

        pDerecho.removeAll()

        val pTabla = tabla.getPanelBar(0, 0, (valorVuelta + 3) * 70, (cantidadMonedas + 2) * 40)
        pTabla.background = semiDarkGray2
        pTabla.border = semiDarkGrayBlueBorder
        pTabla.verticalScrollBar.setUI(getCustomScroll())
        pDerecho.add(pTabla)

        //repaint
        sPDerecho.horizontalScrollBar.value = if(sPDerecho.horizontalScrollBar.value == 0) 1 else 0
        sPDerecho.verticalScrollBar.value = if(sPDerecho.verticalScrollBar.value == 0) 1 else 0

        //label que dice si no hubo cambio o vueltas
        lResultado.setProperties(
            50, 395, 300, 32,
            if (modelo.getValueAt(cantidadMonedas-1, valorVuelta).toString() == "∞") {
                sPDerecho.setLocation(2000, 0)
                "No se pueden dar las vueltas"
            } else {
                JOptionPane.showMessageDialog(
                    null, calcularComposicion(algoritmo.composicion[cantidadMonedas][valorVuelta],
                        modelo.getValueAt(cantidadMonedas-1, valorVuelta).toString())
                )
                ""
            }
        )

    }

    private fun calcularComposicion(composicion: String, dato: String): String{
        if(dato == "∞")
            return "No se pueden dar las vueltas para este caso"
        if(dato == "0")
            return "No se seleccionaron monedas para calcular este valor"

        var itemList = "La cantidad minima de monedas se obtuvo através de:\n"

        itemList += "$composicion\nDonde n:m significa n monedas de valor m"

        return itemList
    }

}