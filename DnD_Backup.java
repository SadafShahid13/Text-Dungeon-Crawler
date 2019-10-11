
import java.util.Random;
import java.util.Scanner;
public class DnD_Backup {

    private static class Actor {

        protected String name;
        protected int health;
        protected int damage;

        public Actor(final String name, final int health, final int damage) {
            this.name = name;
            this.health = health;
            this.damage = damage;
        }

        public void attack(final Actor actor) {
            final int damage = getDamage();
            if (this instanceof Player) {
                System.out.println("\t> You strike the " + actor + " for " + damage + " damage.");
            } else {
                System.out.println("\t> The " + this + " hits you for " + damage + ".");
            }
            actor.damage(damage);
        }

        public void damage(final int damage) {
            health -= damage;
        }

        public int getDamage() {
            return rand.nextInt(damage);
        }

        public boolean isAlive() {
            return health > 0;
        }

        public boolean isDying() {
            return !isAlive();
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private static class Enemy extends Actor {

        private static final String[] ENEMIES = { "Skeleton", "Zombie", "Warrior", "Assassin" };
        private static final int MAX_HEALTH = 50;
        private static final int DAMAGE = 25;

        public static Enemy spawnRandomEnemy() {
            return new Enemy(ENEMIES[rand.nextInt(ENEMIES.length)], rand.nextInt(MAX_HEALTH), DAMAGE);
        }

        public Enemy(final String name, final int health, final int damage) {
            super(name, health, damage);
        }

        public void dropPotionFor(final Player player) {
            if (rand.nextInt(100) < HEALTH_POTION_DROP_PERCENTAGE) {
                System.out.println(" # The " + this + " dropped a health potion! #");
                System.out.println(" # You have " + (++player.healthPots) + " health potion(s). #");
            }
        }
    }

    private final static class Player extends Actor {

        int healthPots;

        public Player(final String name, final int health, final int damage) {
            super(name, health, damage);
            healthPots = 3;
        }

        public void consumePotion() {
            if (hasPotions()) {
                health += HEALTH_POTION_HEAL_AMOUNT;
                healthPots--;
                System.out
                        .println("\t> You drink a health potion, healing yourself for " + HEALTH_POTION_HEAL_AMOUNT + ".");
                System.out.println("\t> You now have " + health + "HP.");
                System.out.println("\t> You have " + healthPots + " health potions remaining. \n");
            } else {
                System.out.println("\t> You have no health potions left! Defeat enemies for a chance to get more! \n");
            }
        }

        public void defeat(final Enemy enemy) {
            System.out.println("------------------------------------------");
            System.out.println(" # " + enemy + " was defeated! # ");
            System.out.println(" # You have " + health + " HP left. #");
            enemy.dropPotionFor(this);
        }

        public boolean hasPotions() {
            return healthPots > 0;
        }
    }

    private static final int HEALTH_POTION_HEAL_AMOUNT = 30;
    private static final int HEALTH_POTION_DROP_PERCENTAGE = 50;
    private static final String ATTACK = "1";
    private static final String USE_POTION = "2";
    private static final String RUN = "3";

    private static final Random rand = new Random();

    public static void main(final String[] args) {
        final Player player = new Player("Player", 125, 50);
        try (final Scanner in = new Scanner(System.in)) {

            System.out.println("Welcome to the Dungeon");

            while (player.isAlive()) {
                System.out.println("------------------------------------------");
                final Enemy enemy = Enemy.spawnRandomEnemy();
                System.out.println("\t# " + enemy + " appeared! #\n");

                while (enemy.isAlive()) {
                    System.out.println("\tYour HP: " + player.health);
                    System.out.println("\t" + enemy + "'s HP: " + enemy.health);
                    System.out.println("\n\tWhat would you like to do?");
                    System.out.println("\t1. Attack");
                    System.out.println("\t2. Drink Health Potion");
                    System.out.println("\t3. Run!");
                    final String input = in.nextLine();
                    if (input.equals(ATTACK)) {
                        player.attack(enemy);
                        enemy.attack(player);
                        if (player.isDying()) {
                            System.out.println("\t> You have taken too much damage, you are too weak to go on!");
                            break;
                        }
                    } else if (input.equals(USE_POTION)) {
                        player.consumePotion();
                    } else if (input.equals(RUN)) {
                        System.out.println("\t> You run away from the " + enemy + "!");
                        continue;
                    } else {
                        System.out.println("\t>Invalid command");
                    }
                }
                player.defeat(enemy);
                System.out.println("------------------------------------------");
                System.out.println("What would you like to do now?");
                System.out.println("1. Continue fighting");
                System.out.println("2. Exit dungeon");
                String input = in.nextLine();
                while (!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid command!");
                    input = in.nextLine();
                }
                if (input.equals("1")) {
                    System.out.println("Continue on your adventure!");
                } else if (input.equals("2")) {
                    System.out.println("You exited the dungeon successfully");
                }
            }
        }
        if (player.isDying()) {
            System.out.println("You limp out of the dungeon, weak from battle.");
        }
        System.out.println("#######################");
        System.out.println("# THANKS FOR PLAYING! #\n");

    }
}