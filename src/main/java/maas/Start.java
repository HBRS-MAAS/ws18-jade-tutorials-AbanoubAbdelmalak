package maas;

import java.util.List;
import java.util.Vector;

import jade.wrapper.AgentController;

public class Start {
    public static void main(String[] args) {
    	List<String> agents = new Vector<>();
    	List<String> cmd = new Vector<>();
    	agents.add("seller1:maas.tutorials.BookSellerAgent");
    	cmd.add("-agents");cmd.add("-agents");cmd.add("-agents");
    	agents.add("seller2:maas.tutorials.BookSellerAgent");
    	agents.add("seller3:maas.tutorials.BookSellerAgent");
    	for (int i = 0; i <=0;i++) {
    		agents.add("buyer"+i+":maas.tutorials.BookBuyerAgent("+"BMW "+")");
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