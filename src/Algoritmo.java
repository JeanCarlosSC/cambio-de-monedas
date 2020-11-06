import java.util.Arrays;

public class Algoritmo {

    private final String[][] composicion;
    private final double[][] matrizResultante;
    private final int[] valores;

    public Algoritmo(int cantidadVueltas, int[] valores) {

        matrizResultante = new double[valores.length+1][cantidadVueltas+1];
        composicion = new String[valores.length+1][cantidadVueltas+1];
        this.valores = valores;
        calcular();

    }

    public double min(double num1,double num2,int i,int j){
        if(num1 < num2){
            composicion[i][j] = String.valueOf(composicion[i-1][j]);
            return num1;
        }
        composicion[i][j] = "1:" + valores[i - 1] + "+\n" + composicion[i][j - valores[i - 1]];
        return num2;
    }

    public void calcular(){

        Arrays.fill(matrizResultante[0], Double.POSITIVE_INFINITY);

        for(int z = 1; z < matrizResultante.length; z++){
            matrizResultante[z][0] = 0;
            composicion[z][0] = "0:0";
        }

        for (String[] strings : composicion) {
            Arrays.fill(strings, "0:0");
        }

        for(int i = 1; i < matrizResultante.length; i++){
            for(int j = 1; j < matrizResultante[0].length; j++ ){
                if(i == 1 &&  j < valores[i-1]){
                    matrizResultante[i][j] = (int) Double.POSITIVE_INFINITY;
                }else if(i == 1){
                    matrizResultante[i][j] = 1 + matrizResultante[1][j- valores[0]];
                    composicion[i][j] = "1:" + valores[0] + "+\n" + composicion[1][j - valores[0]];

                }else if(j < valores[i-1]){
                    matrizResultante[i][j] = matrizResultante[i-1][j];
                    composicion[i][j] = composicion[i-1][j];

                }else{
                    matrizResultante[i][j] = min(matrizResultante[i-1][j],1+matrizResultante[i][j-valores[i-1]],i,j);
                }
            }
        }
    }

    public double[][] getMatriz(){
        return matrizResultante;
    }

    public String[][] getComposicion() {
        return composicion;
    }

}
