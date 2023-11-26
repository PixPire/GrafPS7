import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class CanvasPanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    int heightDif = 68;


    ArrayList<PolygonFigure> figureList=new ArrayList<>();


    private int width=2000;
    private int height=500;


    public CanvasPanel(){
        setDoubleBuffered(false);
        setVisible(true);

    }
    @Override
    public void paint(Graphics g){
        paintComponent(g);

    }
    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            System.out.println("Image=null, drawing a clear Image");

            image = createImage(width, height);
            g2 = (Graphics2D) image.getGraphics();

            clear();
        }
        g.drawImage(image, 0, 0, null);


    }

    public void clear() {
        System.out.println("Clearing");
        g2.setPaint(Color.white);

        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();

    }


    public void PaintFigures(){

        clear();

        g2.setColor(Color.black);



        for (PolygonFigure figure:figureList
             ) {
            System.out.println("Sprawdzam figurę");
            if(figure.pointsList.size()>1) {
                for (int i = 0; i < figure.pointsList.size()-1; i++) {
                    System.out.println("Rysowanie boku nr " + i + " z X1: " + figure.pointsList.get(i).x + " Y1: " + figure.pointsList.get(i).y + " do X2: " + figure.pointsList.get(i + 1).x + " Y2: " + figure.pointsList.get(i + 1).y);
                    g2.drawLine(figure.pointsList.get(i).x, figure.pointsList.get(i).y-heightDif, figure.pointsList.get(i + 1).x, figure.pointsList.get(i + 1).y-heightDif);
                }
                if(figure.pointsList.size()==figure.pointsAmount)g2.drawLine(figure.pointsList.get(figure.pointsList.size()-1).x, figure.pointsList.get(figure.pointsList.size()-1).y-68, figure.pointsList.get(0).x, figure.pointsList.get(0).y-68);
            }

        }
    }








    public void setFigureSet(ArrayList<PolygonFigure> newList) {
        figureList = newList;
        PaintFigures();
        repaint();
    }
}
