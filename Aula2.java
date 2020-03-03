import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Aula2 extends JPanel{ 
	JLabel tecla, espaco1, espaco2, espaco3;
	JTextField textfield;
	JButton um, dois, tres, quatro, cinco, seis, sete, oito, nove, zero, mais, menos, igual, barra, asterisco, ponto;
    
    public Aula2() {
    	inicializarComponentes();
    }
    
    public void inicializarComponentes() {
		//GridLayout
		String[][] teclas_calculadora = new String[][] {
			{"7", "8", "9", "+"},
			{"4", "5", "6", "-"},
			{"1", "2", "3", "="},
			{"0", "*", "/", "."} 
		};
		
		um = new JButton("1");
		dois = new JButton("2");
		tres = new JButton("3");
		quatro = new JButton("4");
		cinco = new JButton("5");
		seis = new JButton("6");
		sete = new JButton("7");
		oito = new JButton("8");
		nove = new JButton("9");
		zero = new JButton("0");
		mais = new JButton("+");
		menos = new JButton("-");
		igual = new JButton("=");
		barra = new JButton("/");
		asterisco = new JButton("*");
		ponto = new JButton(".");
		
		setLayout(new GridLayout(5, 4, 5, 5));
		espaco1 = new JLabel("");
		espaco2 = new JLabel("");
		espaco3 = new JLabel("");
		textfield = new JTextField();

		add(espaco1);
		add(textfield);
		add(espaco2);
		add(espaco3);
		
		add(sete);
		add(oito);
		add(nove);
		add(mais);
		add(quatro);
		add(cinco);
		add(seis);
		add(menos);
		add(um);
		add(dois);
		add(tres);
		add(igual);
		add(zero);
		add(asterisco);
		add(barra);
		add(ponto);
    }

    public static void main(String[] args) {
		JFrame frame = new JFrame("Teclas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Aula2());
		frame.setBounds(450, 300, 320, 400);
		frame.setVisible(true);
    }
}
