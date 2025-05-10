import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

public class GUI extends JFrame {

    //Card randomizer
    Random rand = new Random();

    //integer used for used status
    int tempC;

    //indicatator whether the dealer is thinking or not
    boolean dHitter = false;

    //card list
    ArrayList<Card> Cards = new ArrayList<> ( );

    //list of messages
    ArrayList<Message> Log = new ArrayList<> ( );

    //fonts
    Font fontCard = new Font("Times New Roman", Font.PLAIN, 40);
    Font fontQuest = new Font("Times New Roman", Font.BOLD, 40);
    Font fontButton = new Font("Times New Roman", Font.PLAIN, 25);
    Font fontLog = new Font("Times New Roman", Font.ITALIC, 30);

    //Log message colors
    Color cDealer = Color.blue;
    Color cPlayer = new Color(255,255,255);

    //strings used
    String questHitStay = "Hit or Stand?";
    String questPlayMore = "Play more?";

    //colors used
    Color colorBackground = new Color(113,122,119);
    Color colorButton = new Color(0,100,0);

    //buttons used
    JButton bHit = new JButton();
    JButton bStay = new JButton();
    JButton bYes = new JButton();
    JButton bNo = new JButton();

    int sW = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int sH = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    //window resolution
    int aW = 1300;
    int aH = 800;

    //card grid position and dimensions
    int gridX = 50;
    int gridY = 50;
    int gridW = 900;
    int gridH = 400;

    //card spacing and dimensions
    int spacing = 10;
    int rounding = 10;
    int tCardW = gridW /6;
    int tCardH = gridH /2;
    int cardW = tCardW - spacing*2;
    int cardH = tCardH - spacing*2;

    //booleans about phases
    boolean hit_stay_q = true;
    boolean dealer_turn = false;
    boolean play_more_q = false;

    //player and dealer card array
    ArrayList<Card> pCards = new ArrayList<> ( );
    ArrayList<Card> dCards = new ArrayList<> ( );

    //player and dealer totals
    int pMinTotal = 0;
    int pMaxTotal = 0;
    int dMinTotal = 0;
    int dMaxTotal = 0;

    //polygons for diamond shapes
    int[] polyX = new int[4];
    int[] polyY = new int[4];

    public GUI() {
        this.setTitle("Blackjack");
        this.setBounds((sW-aW-6)/2, (sH-aH-29)/2, aW+6, aH+29);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        Board board = new Board();
        this.setContentPane(board);
        board.setLayout(null);

        Move move = new Move ( );
        this.addMouseMotionListener(move);

        Click click = new Click ( );
        this.addMouseListener(click);

        //button stuff
        ActHit actHit = new ActHit();
        bHit.addActionListener(actHit);
        bHit.setBounds(1000, 200, 120, 50);
        bHit.setBackground(colorButton);
        bHit.setFont(fontButton);
        bHit.setText("HIT");
        board.add(bHit);

        ActStay actStay = new ActStay();
        bStay.addActionListener(actStay);
        bStay.setBounds(1150, 200, 120, 50);
        bStay.setBackground(colorButton);
        bStay.setFont(fontButton);
        bStay.setText("STAND");
        board.add(bStay);

        ActYes actYes = new ActYes();
        bYes.addActionListener(actYes);
        bYes.setBounds(1000, 600, 100, 50);
        bYes.setBackground(colorButton);
        bYes.setFont(fontButton);
        bYes.setText("YES");
        board.add(bYes);

        ActNo actNo = new ActNo();
        bNo.addActionListener(actNo);
        bNo.setBounds(1150, 600, 100, 50);
        bNo.setBackground(colorButton);
        bNo.setFont(fontButton);
        bNo.setText("NO");
        board.add(bNo);

        //creating all cards
        String temp_str;
        for (int i = 0; i < 52; i++) {
            if (i % 4 == 0) {
                temp_str = "Spades";
            } else if (i % 4 == 1) {
                temp_str = "Hearts";
            } else if (i % 4 == 2) {
                temp_str = "Diamonds";
            } else {
                temp_str = "Clubs";
            }
            Cards.add(new Card((i/4) + 1, temp_str, i));
        }


        tempC = rand.nextInt(52);
        pCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();

        do {
            tempC = rand.nextInt (52);
        } while (Cards.get (tempC).used);
        dCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();

        do {
            tempC = rand.nextInt (52);
        } while (Cards.get (tempC).used);
        pCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();

        do {
            tempC = rand.nextInt (52);
        } while (Cards.get (tempC).used);
        dCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();
    }

    public void totalsChecker() {

        int acesCount;

        //calculation of player's totals
        pMinTotal = 0;
        pMaxTotal = 0;
        acesCount = 0;

        for (Card c : pCards) {
            pMinTotal += c.value;
            pMaxTotal += c.value;
            if (Objects.equals (c.name, "Ace"))
                acesCount++;

        }

        if (acesCount > 0)
            pMaxTotal += 10;

        dMinTotal = 0;
        dMaxTotal = 0;
        acesCount = 0;

        for (Card c : dCards) {
            dMinTotal += c.value;
            dMaxTotal += c.value;
            if (Objects.equals (c.name, "Ace"))
                acesCount++;

        }

        if (acesCount > 0)
            dMaxTotal += 10;
    }

    public void setWinner() {
        int pPoints;
        int dPoints;

        if (pMaxTotal > 21) {
            pPoints = pMinTotal;
        } else {
            pPoints = pMaxTotal;
        }

        if (dMaxTotal > 21) {
            dPoints = dMinTotal;
        } else {
            dPoints = dMaxTotal;
        }

        if (pPoints > 21 && dPoints > 21) {
            Log.add(new Message("Nobody wins!", "Dealer"));
        } else if (dPoints > 21) {
            Log.add(new Message("You win!", "Player"));
            Main.pWins++;
        } else if (pPoints > 21) {
            Log.add(new Message("Dealer wins!", "Dealer"));
            Main.dWins++;
        } else if (pPoints > dPoints) {
            Log.add(new Message("You win!", "Player"));
            Main.pWins++;
        } else {
            Log.add(new Message("Dealer wins!", "Dealer"));
            Main.dWins++;
        }

    }

    public void dealerHitStay() {
        dHitter = true;

        int dAvailable;
        if (dMaxTotal > 21) {
            dAvailable = dMinTotal;
        } else {
            dAvailable = dMaxTotal;
        }

        int pAvailable;
        if (pMaxTotal > 21) {
            pAvailable = pMinTotal;
        } else {
            pAvailable = pMaxTotal;
        }

        repaint();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ((dAvailable < pAvailable && pAvailable <= 21) || dAvailable < 16) {
            int tempMax;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer decided to hit! (total: " + tempMax + ")");
            Log.add(new Message(mess, "Dealer"));
            do {
                tempC = rand.nextInt (52);
            } while (Cards.get (tempC).used);
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();
            } else {
            int tempMax;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer decided to stand! (total: " + tempMax + ")");
            Log.add(new Message(mess, "Dealer"));
            setWinner();
            dealer_turn = false;
            play_more_q = true;
        }
        dHitter = false;
    }

    public void refresher() {

        if (hit_stay_q) {
            bHit.setVisible(true);
            bStay.setVisible(true);
        } else {
            bHit.setVisible(false);
            bStay.setVisible(false);
        }

        if (dealer_turn) {
            if (!dHitter)
                dealerHitStay();
        }

        if (play_more_q) {
            bYes.setVisible(true);
            bNo.setVisible(true);
        } else {
            bYes.setVisible(false);
            bNo.setVisible(false);
        }

        totalsChecker();

        if ((pMaxTotal == 21 || pMinTotal >= 21) && hit_stay_q) {
            int tempMax;
            if (pMaxTotal <= 21) {
                tempMax = pMaxTotal;
            } else {
                tempMax = pMinTotal;
            }
            String mess = ("Pass (total: " + tempMax + ")");
            Log.add(new Message(mess, "Player"));
            hit_stay_q = false;
            dealer_turn = true;
        }

        if ((dMaxTotal == 21 || dMinTotal >= 21) && dealer_turn) {
            int tempMax;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer Pass!(total: " + tempMax + ")");
            Log.add(new Message(mess, "Dealer"));
            setWinner();
            dealer_turn = false;
            play_more_q = true;
        }

        repaint();
    }

    public class Board extends JPanel {

        public void paintComponent(Graphics g) {
            //background
            g.setColor(colorBackground);
            g.fillRect(0, 0, aW, aH);

            //questions
            if (hit_stay_q) {
                g.setColor(Color.black);
                g.setFont(fontQuest);
                g.drawString(questHitStay, gridX+gridW+60, gridY+90);
                g.drawString("Total:", gridX+gridW+60, gridY+290);
                if (pMinTotal == pMaxTotal) {
                    g.drawString(Integer.toString(pMaxTotal), gridX+gridW+60, gridY+350);
                } else if (pMaxTotal <= 21) {
                    g.drawString(pMinTotal + " or " + pMaxTotal, gridX+gridW+60, gridY+350);
                } else {
                    g.drawString(Integer.toString(pMinTotal), gridX+gridW+60, gridY+350);
                }
            } else if (play_more_q) {
                g.setColor(Color.black);
                g.setFont(fontQuest);
                g.drawString(questPlayMore, gridX+gridW+70, gridY+490);
            }

            g.setColor(Color.black);
            g.fillRect(gridX, gridY+gridH+50, gridW, 500);

            //Log
            g.setFont(fontLog);
            int logIndex = 0;
            for (Message L : Log) {
                if (L.getWho().equalsIgnoreCase("Dealer")) {
                    g.setColor(cDealer);
                } else {
                    g.setColor(cPlayer);
                }
                g.drawString(L.getMessage(), gridX+20, gridY+480+logIndex*35);
                logIndex++;
            }

            //score
            g.setColor(Color.BLACK);
            g.setFont(fontQuest);
            String score = ("Score: " + Main.pWins + " - " + Main.dWins);
            g.drawString(score, gridX+gridW+70, gridY+gridH+300);

            //player cards
            int index = 0;
            for (Card c : pCards) {
                g.setColor(Color.white);
                g.fillRect(gridX+spacing+tCardW*index+rounding, gridY+spacing, cardW-rounding*2, cardH);
                g.fillRect(gridX+spacing+tCardW*index, gridY+spacing+rounding, cardW, cardH-rounding*2);
                g.fillOval(gridX+spacing+tCardW*index, gridY+spacing, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);

                g.setFont(fontCard);
                if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }

                g.drawString(c.symbol, gridX+spacing+tCardW*index+rounding, gridY+spacing+cardH-rounding);

                if (c.shape.equalsIgnoreCase("Hearts")) {
                    g.fillOval(gridX+tCardW*index+42, gridY+70, 35, 35);
                    g.fillOval(gridX+tCardW*index+73, gridY+70, 35, 35);
                    g.fillArc(gridX+tCardW*index+30, gridY+90, 90, 90, 51, 78);
                } else if (c.shape.equalsIgnoreCase("Diamonds")) {
                    polyX[0] = gridX+tCardW*index+75;
                    polyX[1] = gridX+tCardW*index+50;
                    polyX[2] = gridX+tCardW*index+75;
                    polyX[3] = gridX+tCardW*index+100;
                    polyY[0] = gridY+60;
                    polyY[1] = gridY+100;
                    polyY[2] = gridY+140;
                    polyY[3] = gridY+100;
                    g.fillPolygon(polyX, polyY, 4);
                } else if (c.shape.equalsIgnoreCase("Spades")) {
                    g.fillOval(gridX+tCardW*index+42, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*index+73, gridY+90, 35, 35);
                    g.fillArc(gridX+tCardW*index+30, gridY+15, 90, 90, 51+180, 78);
                    g.fillRect(gridX+tCardW*index+70, gridY+100, 10, 40);
                } else {
                    g.fillOval(gridX+tCardW*index+40, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*index+75, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*index+58, gridY+62, 35, 35);
                    g.fillRect(gridX+tCardW*index+70, gridY+75, 10, 70);
                }

                index++;
            }

            if (dealer_turn || play_more_q) {
                //dealer cards
                index = 0;
                for (Card c : dCards) {
                    g.setColor(Color.white);
                    g.fillRect(gridX+spacing+tCardW*index+rounding, gridY+spacing+200, cardW-rounding*2, cardH);
                    g.fillRect(gridX+spacing+tCardW*index, gridY+spacing+rounding+200, cardW, cardH-rounding*2);
                    g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);

                    g.setFont(fontCard);
                    if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.black);
                    }

                    g.drawString(c.symbol, gridX+spacing+tCardW*index+rounding, gridY+spacing+cardH-rounding+200);

                    if (c.shape.equalsIgnoreCase("Hearts")) {
                        g.fillOval(gridX+tCardW*index+42, gridY+70+200, 35, 35);
                        g.fillOval(gridX+tCardW*index+73, gridY+70+200, 35, 35);
                        g.fillArc(gridX+tCardW*index+30, gridY+90+200, 90, 90, 51, 78);
                    } else if (c.shape.equalsIgnoreCase("Diamonds")) {
                        polyX[0] = gridX+tCardW*index+75;
                        polyX[1] = gridX+tCardW*index+50;
                        polyX[2] = gridX+tCardW*index+75;
                        polyX[3] = gridX+tCardW*index+100;
                        polyY[0] = gridY+60+200;
                        polyY[1] = gridY+100+200;
                        polyY[2] = gridY+140+200;
                        polyY[3] = gridY+100+200;
                        g.fillPolygon(polyX, polyY, 4);
                    } else if (c.shape.equalsIgnoreCase("Spades")) {
                        g.fillOval(gridX+tCardW*index+42, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*index+73, gridY+90+200, 35, 35);
                        g.fillArc(gridX+tCardW*index+30, gridY+15+200, 90, 90, 51+180, 78);
                        g.fillRect(gridX+tCardW*index+70, gridY+100+200, 10, 40);
                    } else {
                        g.fillOval(gridX+tCardW*index+40, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*index+75, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*index+58, gridY+62+200, 35, 35);
                        g.fillRect(gridX+tCardW*index+70, gridY+75+200, 10, 70);
                    }

                    index++;
                }

                g.setColor(Color.black);
                g.setFont(fontQuest);
                g.drawString("Your total: ", gridX+gridW+60, gridY+40);
                if (pMaxTotal <= 21) {
                    g.drawString(Integer.toString(pMaxTotal), gridX+gridW+60, gridY+120);
                } else {
                    g.drawString(Integer.toString(pMinTotal), gridX+gridW+60, gridY+120);
                }
                g.drawString("Dealer's total: ", gridX+gridW+60, gridY+240);
                if (dMaxTotal <= 21) {
                    g.drawString(Integer.toString(dMaxTotal), gridX+gridW+60, gridY+320);
                } else {
                    g.drawString(Integer.toString(dMinTotal), gridX+gridW+60, gridY+320);
                }
            }

        }

    }

    public static class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent arg0) {

        }

        @Override
        public void mouseMoved(MouseEvent arg0) {

        }

    }

    public static class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {

        }

        @Override
        public void mouseEntered(MouseEvent arg0) {

        }

        @Override
        public void mouseExited(MouseEvent arg0) {

        }

        @Override
        public void mousePressed(MouseEvent arg0) {

        }

        @Override
        public void mouseReleased(MouseEvent arg0) {

        }

    }

    public class ActHit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (hit_stay_q) {

                int tempMax;
                if (pMaxTotal <= 21) {
                    tempMax = pMaxTotal;
                } else {
                    tempMax = pMinTotal;
                }
                String mess = ("You decided to hit! (total: " + tempMax + ")");
                Log.add(new Message(mess, "Player"));

                do {
                    tempC = rand.nextInt (52);
                } while (Cards.get (tempC).used);
                pCards.add(Cards.get(tempC));
                Cards.get(tempC).setUsed();
                }
        }

    }

    public class ActStay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (hit_stay_q) {

                int tempMax;
                if (pMaxTotal <= 21) {
                    tempMax = pMaxTotal;
                } else {
                    tempMax = pMinTotal;
                }
                String mess = ("You decided to stay! (total: " + tempMax + ")");
                Log.add(new Message(mess, "Player"));

                hit_stay_q = false;
                dealer_turn = true;
            }
        }

    }

    public class ActYes implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            for (Card c : Cards) {
                c.setNotUsed();
            }

            pCards.clear();
            dCards.clear();
            Log.clear();

            play_more_q = false;
            hit_stay_q = true;

            tempC = rand.nextInt(52);
            pCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();

            do {
                tempC = rand.nextInt (52);
            } while (Cards.get (tempC).used);
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();

            do {
                tempC = rand.nextInt (52);
            } while (Cards.get (tempC).used);
            pCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();

            do {
                tempC = rand.nextInt (52);
            } while (Cards.get (tempC).used);
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();

        }

    }

    public class ActNo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Main.terminator = true;
            dispose();
        }

    }

}
