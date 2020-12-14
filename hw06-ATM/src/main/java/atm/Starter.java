package atm;

import atm.atm.ATM;
import atm.atm.impl.ATMImpl;

import java.util.List;
import java.util.Scanner;

public class Starter {
    private ATM atm;

    public static void main(String args[]) {
        Starter starter = new Starter();
        starter.atm = ATMImpl.ATMImplBuilder.build();
        String name = "User";
        System.out.println("Hello, my dear friend. What's your name?");
        Scanner scanner = new Scanner(System.in);
        Scanner stringScanner;
        name = scanner.nextLine();
        System.out.println("Hello " + name + "! What's your command? (add, get, rest, exit)");
        String operation = scanner.nextLine();
        while (!operation.equalsIgnoreCase("exit")) {
            switch (operation.toLowerCase()) {
                case "add":
                    System.out.println("What nominal?");
                    String nominalsString = scanner.nextLine();
                    stringScanner = new Scanner(nominalsString);
                    Integer value;
                    while (stringScanner.hasNextInt()) {
                        value = stringScanner.nextInt();
                        Nominal nominal = Nominal.getNominalFromInt(value);
                        if (nominal != null) {
                            starter.atm.putCash(nominal);
                            System.out.println("Success " + nominal.getNominal());
                        } else {
                            System.out.println(value + "? Was it?");
                        }
                    }
                    break;
                case "get":
                    System.out.println("How much? Enter sum");
                    String sumString = scanner.nextLine();
                    stringScanner = new Scanner(sumString);
                    value = stringScanner.nextInt();
                    List<Nominal> cash = starter.atm.getCash(value);
                    System.out.print("Cash included:");
                    for (Nominal nominal:cash){
                        System.out.print(" " + nominal.getNominal());
                    }
                    System.out.println("");
                    break;
                case "rest":
                    System.out.println("Balance is " + starter.atm.getBalance());
                    break;
                default:
                    System.out.println("Incorrect command");
            }

            System.out.println(" What's your next command? (add, get, rest, exit)");
            operation = scanner.nextLine();
        }
    }
}