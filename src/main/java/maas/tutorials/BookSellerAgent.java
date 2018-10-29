package maas.tutorials;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class BookSellerAgent extends Agent {
  private boolean serverFlag = false;
  private boolean purchaseFlag = false;
  // The catalogue of books for sale (maps the title of a book to its price)
  private Hashtable catalogue;
  // The GUI by means of which the user can add books in the catalogue
  //private BookSellerGui myGui;
  //Put agent initializations here
  protected void setup() {
    System.out.println("Hello! Buyer-agent "+getAID().getName()+" is ready.");
  // Create the catalogue
  catalogue = new Hashtable();
  // Create and show the GUI
  Object[] args = getArguments();
  if (args != null && args.length > 0) {
    for (int k=0; k < args.length; k++) {
      String title = args[k].toString();
      ++k;
      int price = Integer.parseInt(args[k].toString());
      catalogue.put(title, new Integer(price));
      
    }
  }
  //myGui = new BookSellerGui(this);
  //myGui.show();
  DFAgentDescription dfd = new DFAgentDescription();
  dfd.setName(getAID());
  ServiceDescription sd = new ServiceDescription();
  sd.setType("book-selling");
  sd.setName("JADE-book-trading");
  dfd.addServices(sd);
  try {
    DFService.register(this, dfd);
  }
  catch (FIPAException fe) {
    fe.printStackTrace();
  }
  // Add the behaviour serving requests for offer from buyer agents
  addBehaviour(new OfferRequestsServer());
  // Add the behaviour serving purchase orders from buyer agents
  addBehaviour(new PurchaseOrdersServer());
  addBehaviour(new TickerBehaviour(this, 30000) {
    protected void onTick() {
      if (purchaseFlag && serverFlag) {
        myAgent.doDelete();
      }
    }
  });
    }
  
  
  protected void takeDown() {
    try {
      DFService.deregister(this);
    }
    catch (FIPAException fe) {
      fe.printStackTrace();
    }
    // Close the GUI
    //myGui.dispose();
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
	    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
	    ACLMessage msg = myAgent.receive(mt);
  	  if (msg != null) {
  	    serverFlag = false;
  	    // Message received. Process it
  	    String title = msg.getContent();
  	    ACLMessage reply = msg.createReply();
  	    Integer price = (Integer) catalogue.get(title);
  	    if (price != null) {
  	      // The requested book is available for sale. Reply with the price
  	      reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
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
  	    serverFlag = true;
  	    block();
  	  }
  	  }
  }
  
  
  private class PurchaseOrdersServer extends CyclicBehaviour{  
    public void action() {
      ACLMessage request = myAgent.receive();
      if (request != null) {
        purchaseFlag = false;
        ACLMessage reply = request.createReply();
        Integer price = (Integer) catalogue.get(request.getContent());
        if (price != null) {
          // The requested book is available for sale. Reply with the price
          reply.setPerformative(ACLMessage.INFORM);
          reply.setContent(String.valueOf(price.intValue()));
          myAgent.send(reply);
        }
        else {
          // The requested book is NOT available for sale.
          reply.setPerformative(ACLMessage.REFUSE);
          reply.setContent("not-available");
          myAgent.send(reply);
        }
        myAgent.send(reply);
      }
      else {
        purchaseFlag = true;
        block();
      }
    }
  }
  
}
