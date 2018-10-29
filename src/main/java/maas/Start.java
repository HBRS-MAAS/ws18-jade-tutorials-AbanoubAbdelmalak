package maas;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import jade.wrapper.AgentController;

public class Start {
    public static void main(String[] args) {
      Random generator = new Random();
    	List<String> agents = new Vector<>();
    	List<String> cmd = new Vector<>();
    	String[] books = {"1984", "Brave new World", "Hamlet","Romeo and Juliet","Macbeth","Enders Game","StarWars"};
    	agents.add("seller1:maas.tutorials.BookSellerAgent(1984,40,Brave new World,30,Hamlet,45,Romeo and Juliet,34)");
    	cmd.add("-agents");cmd.add("-agents");cmd.add("-agents");
    	agents.add("seller2:maas.tutorials.BookSellerAgent(1984,30,StarWars,30,Macbeth,55,Romeo and Juliet,30)");
    	agents.add("seller3:maas.tutorials.BookSellerAgent(Brave new World,50,Enders Game,25,Macbeth,60,Hamlet,60)");
    	for (int i = 0; i <=19;i++) {
    		agents.add("buyer"+i+":maas.tutorials.BookBuyerAgent("+books[generator.nextInt(books.length)]+","+books[generator.nextInt(books.length)]
    		    +","+books[generator.nextInt(books.length)]+")");
    		cmd.add("-agents");
    	}

    	
    	//cmd.add("-agents");
    	//cmd.add("-args");
    	StringBuilder sb = new StringBuilder();
    	for (String a : agents) {
    		sb.append(a);
    		sb.append(";");
    	}
    	cmd.add(sb.toString());
      jade.Boot.main(cmd.toArray(new String[cmd.size()]));
    }
}