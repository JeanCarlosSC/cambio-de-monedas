import swingRAD.setMainBar
import swingRAD.setProperties
import swingRAD.setBackground
import javax.swing.JFrame
import javax.swing.JPanel

class Window: JFrame() {

    init{
        val pIzquierdo = JPanel()
        

        setMainBar("cambio de monedas")
        setBackground("resources/background/backgroundBlack1.jpg")
        setProperties()
    }

}