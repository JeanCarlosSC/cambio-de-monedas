import swingRAD.*
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.table.DefaultTableModel
import kotlin.math.min
import kotlin.arrayOf as arrayOf1

class Window: JFrame() {
    private val tfValorVuelta = JTextField()
    private val tfCantidadMonedas = JTextField()
    private var aTfValores = arrayOf1<JTextField?>()
    private val pContenedor = JPanel()
    private val pDerecho = JPanel()
    private val sPDerecho = JScrollPane(pDerecho, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
    private val lResultado = JLabel()

    init{
        val pIzquierdo = JPanel()
        pIzquierdo.setProperties(28, 57, 410, 444)
        add(pIzquierdo)

        val taTexto = JTextArea()
        taTexto.setProperties(30, 40, 370, 110, false, text = "si i == 1 y j < valor_i entonces c[i,j] es ∞\n" +
                "sino si i == 1 entonces c[i,j] = 1 + c[1, j-valor_1]\n" +
                "sino si j < valor_i entonces c[i,j] = c[i-1, j]\n" +
                "sino c[i,j] = mínimo ( c[i-1, j], 1 + c[i, j-valor_i] )", background = null, border = null)
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

        pContenedor.setProperties(0, 0, 200, 130, border = null)
        pContenedor.preferredSize = Dimension(200, 130)

        val sValores = JScrollPane(pContenedor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        sValores.setProperties(30, 220, 220, 150)
        pIzquierdo.add(sValores)

        pDerecho.setProperties(0, 0, 767, 630, border = transparentBorder, background = transparent)

        sPDerecho.setProperties(478, 57, 767, 630, background = transparent, border = transparentBorder)
        add(sPDerecho)

        val bCambio = JButton()
        bCambio.addActionListener { cambio() }
        bCambio.setProperties(260, 257, 100, 32, "Cambio")
        pIzquierdo.add(bCambio)

        setMainBar("cambio de monedas")
        setBackground("resources/background/backgroundBlack1.jpg")
        setProperties()
    }

    private fun abrirCampos(){
        pContenedor.removeAll()
        pContenedor.setBounds(0, 0, 20, 40+32*tfCantidadMonedas.text.toInt())
        pContenedor.preferredSize = Dimension(200, 40+32*tfCantidadMonedas.text.toInt())

        aTfValores = Array(tfCantidadMonedas.text.toInt()){null}
        for(i in 0 until tfCantidadMonedas.text.toInt()) {
            val tfValori = JTextField()
            tfValori.setProperties(110, 20 + 32*i, 80, 26)
            aTfValores[i] = tfValori
            pContenedor.add(aTfValores[i])

            val lValor = JLabel()
            lValor.setProperties(20, 18 + 32*i, 100, 32, "Valor ${i+1}")
            pContenedor.add(lValor)
        }

        repaint()
    }

    private fun cambio(){
        sPDerecho.setBounds(478, 57, 767, 630)
        sPDerecho.border = semiDarkGray2Border
        sPDerecho.background = semiDarkGrayBlue
        pDerecho.background = semiDarkGrayBlue

        pDerecho.setBounds(478, 57, (tfValorVuelta.text.toInt()+3)*70, (tfCantidadMonedas.text.toInt()+2)*40)
        pDerecho.preferredSize = Dimension((tfValorVuelta.text.toInt()+3)*70, (tfCantidadMonedas.text.toInt()+2)*40)
        
        val datos = IntArray(aTfValores.size){aTfValores[it]!!.text.toInt()}
        datos.sort()

        //diseño de la tabla----------------------------------------------------------------------
        val placeholdes = arrayListOf("Valores")
        for(i in 0 .. tfValorVuelta.text.toInt())
            placeholdes.add(i.toString())
        placeholdes.add("Fila")
        val cabecera: Array<String> = placeholdes.toArray(arrayOfNulls<String>(0))

        val modelo = DefaultTableModel()
        modelo.setColumnIdentifiers(cabecera)
        for (i in 0 .. tfCantidadMonedas.text.toInt()) {
            modelo.addRow(arrayOf1<Any>(""))
            for(j in 0 .. tfValorVuelta.text.toInt()+2){
                //primera columna
                if(j == 0)
                    if(i > 0)
                        modelo.setValueAt(datos[i-1], i, j)

                //segunda columna
                if(j == 1)
                    if(i > 0)
                        modelo.setValueAt(0, i, j)

                //valores intermedios
                if(j>0 && j < tfValorVuelta.text.toInt()+2) {
                    //primera fila
                    if(i == 0)
                        modelo.setValueAt("∞", i, j)

                    //los datos restantes
                    if(j>1 && i>0){
                        if(i == 1 && j <= datos[i-1])
                            modelo.setValueAt("∞", i, j)
                        else if(i == 1)
                            modelo.setValueAt(
                                if(modelo.getValueAt(1, j-datos[i-1]).toString() != "∞")
                                    1 + modelo.getValueAt(1, j-datos[i-1]).toString().toInt()
                                else
                                    "∞", i, j
                            )
                        else if(j < datos[i-1])
                            modelo.setValueAt(modelo.getValueAt(i-1, j), i, j)
                        else
                            modelo.setValueAt(
                                if(modelo.getValueAt(i-1, j).toString() != "∞" && modelo.getValueAt(i-1, j-datos[i-1]).toString() != "∞")
                                    min(modelo.getValueAt(i-1, j).toString().toInt(), 1 + modelo.getValueAt(i,
                                        j-datos[i-1]).toString().toInt())
                                else if(modelo.getValueAt(i-1, j).toString() != "∞")
                                    modelo.getValueAt(i-1, j).toString().toInt()
                                else if(modelo.getValueAt(i-1, j-datos[i-1]).toString() != "∞")
                                    1 + modelo.getValueAt(i-1, j-datos[i-1]).toString().toInt()
                                else "∞", i, j
                            )
                    }
                    /*else if(i == 1)
                        modelo.setValueAt(1 + modelo.getValueAt(1, j-datos[0]).toString().toInt(), i, j)*/
                }

                //ultima columna
                if(j == tfValorVuelta.text.toInt()+2)
                    modelo.setValueAt(i, i, j)
            }
        }

        val tabla = JTable()
        tabla.model = modelo
        tabla.rowHeight = 40
        tabla.setDefaultRenderer(Any::class.java, getCustomTable())
        tabla.gridColor = black
        tabla.addMouseListener(object: MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                JOptionPane.showMessageDialog(null, calcularComposicion(modelo, tabla.selectedRow, tabla.selectedColumn))
            }

            override fun mousePressed(e: MouseEvent?) {
            }

            override fun mouseReleased(e: MouseEvent?) {
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseExited(e: MouseEvent?) {
            }

        })

        val header = tabla.tableHeader
        header.preferredSize = Dimension(580, 30)
        header.defaultRenderer = getCustomTable(semiDarkGray2, null, null, white, fontText)

        pDerecho.removeAll()

        val pTabla = tabla.getPanelBar(0, 0, (tfValorVuelta.text.toInt()+3)*70, (tfCantidadMonedas.text.toInt()+2)*40)
        pTabla.background = semiDarkGray2
        pTabla.border = semiDarkGrayBlueBorder
        pTabla.verticalScrollBar.setUI(getCustomScroll())
        pDerecho.add(pTabla)

        repaint()

        //----------------------------------------------------------------------------------------

        lResultado.setProperties(
            50, 395, 300, 32,
            if (modelo.getValueAt(tfCantidadMonedas.text.toInt(), tfValorVuelta.text.toInt() + 1).toString() == "∞") {
                sPDerecho.setLocation(2000, 0)
                "No se pueden dar las vueltas"
            } else {
                JOptionPane.showMessageDialog(null, calcularComposicion(modelo, tfCantidadMonedas.text.toInt(),
                    tfValorVuelta.text.toInt()+1))
                ""
            }
        )

        sPDerecho.horizontalScrollBar.value = if(sPDerecho.horizontalScrollBar.value == 0) 1 else 0
        sPDerecho.verticalScrollBar.value = if(sPDerecho.verticalScrollBar.value == 0) 1 else 0
    }

    private fun calcularComposicion(modelo: DefaultTableModel, i: Int, j: Int): String{
        if(modelo.getValueAt(i, j).toString() == "∞")
            return "No se pueden dar las vueltas para este caso"
        if(modelo.getValueAt(i, j).toString().toInt() == 0)
            return "No se seleccionaron monedas para calcular este valor"

        var itemList = "La cantidad minima de monedas se obtuvo através de:\n"
        var list = ""

        //permutacion

        itemList += "$list\nDonde n:m significa n monedas de valor m"

        return itemList
    }

}