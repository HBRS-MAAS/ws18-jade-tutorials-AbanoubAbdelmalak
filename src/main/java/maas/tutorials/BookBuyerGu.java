package maas.tutorials;

public interface BookBuyerGu {
  void setAgent(BookBuyerAgent a);
  void show();
  void hide();
  void notifyUser(String message);
  void dispose();
}
