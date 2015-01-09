//package ui;
//
//import life.game.classes.life.*;
//import life.game.classes.world.LocationType;
//import life.game.classes.world.Node;
//import life.game.classes.world.World;
//import javafx.geometry.Point2D;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//
//import java.util.List;
//
//public class WorldRenderer implements IRender {
//
//    private Canvas canvas;
//    private World world;
//    private Life following;
//    private Node center;
//
//    public WorldRenderer(Canvas canvas, World world) {
//        this.canvas = canvas;
//        this.world = world;
//        this.center = world.getNode(world.getWidth() / 2, world.getHeight() / 2);
//    }
//
//    public WorldRenderer(Canvas canvas, World world, Node center) {
//        this.canvas = canvas;
//        this.world = world;
//        this.center = center;
//    }
//
//    @Override
//    public Node getCenter() {
//        return center;
//    }
//
//    @Override
//    public Life getFollowing() {
//        return following;
//    }
//
//    @Override
//    public void setCenter(Node center)
//    {
//        this.center = center;
//    }
//
//    @Override
//    public void setFollowing(Life life) {
//        this.following = life;
//    }
//
//    @Override
//    public void refresh() {
//        GraphicsContext context = canvas.getGraphicsContext2D();
//
//        if (following != null) {
//            center = following.getNode();
//        }
//
//        int offsetX = center.getX() - world.getWidth() / 2;
//        int offsetY = center.getY() - world.getHeight() / 2;
//
//        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//        Node[][] nodes = world.getNodes();
//        for (int x = 0; x < nodes.length; x++) {
//            for (int y = 0; y < nodes[x].length; y++) {
//                drawNode(nodes[x][y], context, offsetX, offsetY);
//            }
//        }
//
//        List<Life> lives = world.getLives();
//        for (int i = 0; i < lives.size(); i++) {
//            Life life = lives.get(i);
//            if (life instanceof Animal) {
//                drawAnimal((Animal) life, context, offsetX, offsetY);
//            } else if (life instanceof Plant) {
//                drawPlant((Plant) life, context, offsetX, offsetY);
//            }
//        }
//    }
//
//    private int getDrawCoordinate(int coordinate, int offset, int totalLength) {
//        int drawCoordinate = (coordinate - offset) % totalLength;
//        if (drawCoordinate < 0) {
//            drawCoordinate += totalLength;
//        }
//        return  drawCoordinate;
//    }
//
//    private void drawPlant(Plant plant, GraphicsContext context, int offsetX, int offsetY) {
//        Node node = plant.getNode();
//        int x = getDrawCoordinate(node.getX(), offsetX, world.getWidth());
//        int y = getDrawCoordinate(node.getY(), offsetY, world.getHeight());
//        context.setFill(Color.rgb(0, 255 * plant.getEnergy() / Plant.MAX_ENERGY, 0));
//
//        context.fillRect(x, y, 1, 1);
//    }
//
//    private void drawNode(Node node, GraphicsContext context, int offsetX, int offsetY) {
//        int x = getDrawCoordinate(node.getX(), offsetX, world.getWidth());
//        int y = getDrawCoordinate(node.getY(), offsetY, world.getHeight());
//
//        if (node.getLocationType().equals(LocationType.Land)) {
//            context.setFill(javafx.scene.paint.Color.WHITE);
//        } else if (node.getLocationType().equals(LocationType.Obstacle)) {
//            context.setFill(javafx.scene.paint.Color.LIGHTGRAY);
//        } else {
//            context.setFill(javafx.scene.paint.Color.LIGHTBLUE);
//        }
//
//        context.fillRect(x, y, 1, 1);
//    }
//
//    private void drawAnimal(Animal animal, GraphicsContext context, int offsetX, int offsetY) {
//        Digestion digestion = animal.getGenetics().getDigestion();
//
//        Color color;
//        if (digestion.equals(Digestion.Carnivore)) {
//            color = Color.color(0, 1, 1, animal.getHunger() / 100);
//        } else if (digestion.equals(Digestion.Omnivore)) {
//            color = Color.color(1, 1, 1, animal.getHunger() / 100);
//        } else {
//            color = Color.color(1, 0, 1, animal.getHunger() / 100);
//        }
//
//        Node node = animal.getNode();
//        int x = getDrawCoordinate(node.getX(), offsetX, world.getWidth());
//        int y = getDrawCoordinate(node.getY(), offsetY, world.getHeight());
//
//        context.setFill(color);
//        context.fillRect(x + 0.1, y + 0.1, 0.8, 0.8);
//
//        if (animal.getGender().equals(Gender.Female)) {
//            context.setFill(Color.BLUE);
//        } else {
//            context.setFill(Color.RED);
//        }
//
//        context.fillRect(x + 0.3, y + 0.3, 0.4, 0.4);
//    }
//}
