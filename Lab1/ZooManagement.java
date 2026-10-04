class Animal {
    String name;
    double weight;

    Animal(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }
}

class Lion extends Animal {
    double eat;

    Lion(String name, double weight, double eat) {
        super(name, weight);
        this.eat = eat;
    }

    void display() {
        System.out.println("Su tu " + name + " nang " + weight
                + " can va an " + eat + " can thit moi ngay.");
    }
}

class Snake extends Animal {
    double length;

    Snake(String name, double weight, double length) {
        super(name, weight);
        this.length = length;
    }

    void display() {
        System.out.println("Con ran " + name + " nang " + weight
                + " can va dai " + length + " met.");
    }
}

class Monkey extends Animal {
    String favoriteFood;

    Monkey(String name, double weight, String favoriteFood) {
        super(name, weight);
        this.favoriteFood = favoriteFood;
    }

    void display() {
        System.out.println("Con khi " + name + " nang " + weight
                + " can va thich an " + favoriteFood + ".");
    }
}

public class ZooManagement {
    public static void main(String[] args) {
        Lion lion = new Lion("Leo", 300, 5);
        Snake snake = new Snake("Boa", 50, 5);
        Monkey monkey = new Monkey("George", 150, "chuoi");

        lion.display();
        snake.display();
        monkey.display();
    }
}
