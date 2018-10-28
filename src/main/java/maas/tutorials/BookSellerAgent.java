package maas.tutorials;

import jade.core.Agent;
import jade.core.behaviours.*;
import java.util.*;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class BookSellerAgent extends Agent {
  // The catalogue of books for sale (maps the title of a book to its price)
  private Hashtable catalogue;
  // The GUI by means of which the user can add books in the catalogue
  private BookSellerGui myGui;
  //Put agent initializations here
  protected void setup() {
  // Create the catalogue
  catalogue = new Hashtable();
  // Create and show the GUI
  myGui = new BookSellerGui(this);
  myGui.show();
  // Add the behaviour serving requests for offer from buyer agents
  addBehaviour(new OfferRequestsServer());
  // Add the behaviour serving purchase orders from buyer agents
  //addBehaviour(new PurchaseOrdersServer());
  }
  
  protected void takeDown() {
    // Close the GUI
    myGui.dispose();
    // Printout a dismissal message
    System.out.println("Seller-agent "+getAID().getName()+" terminating.");
  }
  
  public void updateCatalogue(final String title, final int price) {
	  addBehaviour(new OneShotBehaviour() {
	  public void action() {
	  catalogue.put(title, new Integer(price));
	  }
	  } );
  }
  private class OfferRequestsServer extends CyclicBehaviour {
	  public void action() {
  	  ACLMessage msg = myAgent.receive();
  	  if (msg != null) {
  	    // Message received. Process it
  	    String title = msg.getContent();
  	    ACLMessage reply = msg.createReply();
  	    Integer price = (Integer) catalogue.get(title);
  	    if (price != null) {
  	      // The requested book is available for sale. Reply with the price
  	      reply.setPerformative(ACLMessage.PROPOSE);
  	      reply.setContent(String.valueOf(price.intValue()));
  	    }
  	    else {
  	      // The requested book is NOT available for sale.
  	      reply.setPerformative(ACLMessage.REFUSE);
  	      reply.setContent("not-available");
  	    }
  	  myAgent.send(reply);
  	  }
  	  else {
  	    block();
  	  }
  	  }
  } 
}
