package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private Controller router;

    public void showMenu() {
        try {
            Scanner sc = new Scanner(System.in);
            String option;
            String subnet;
            String path;
            boolean contExecution = true;
            boolean isRunningRouter = false;
            while (contExecution) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Start router");
                System.out.println("2. Stop router");
                System.out.println("3. Add subnet");
                System.out.println("4. Show routes");
                System.out.println("5. Open logbook");
                System.out.println("6. Exit");
                System.out.print("Option: ");
                System.out.flush();
                option = sc.nextLine();
                switch (option) {
                    case "1":
                        System.out.println("\nStarting router");
                        if (!isRunningRouter) {
                            System.out.printf("Insert the file name: ");
                            path = sc.nextLine();
                            this.router = new Controller(path);
                            isRunningRouter = true;
                        } else {
                            System.out.println("The router is running!!");
                        }
                        break;
                    case "2":
                        System.out.println("\nStopping router");
                        if (isRunningRouter) {
                            this.router.stopClients();
                            this.router.stopServers();
                            isRunningRouter = false;
                        } else {
                            System.out.println("The router is not running!!");
                        }
                        break;
                    case "3":
                        System.out.flush();
                        System.out.print("\nEnter the subnet you want to add: ");
                        subnet = sc.nextLine();
                        if (isRunningRouter) {
                            this.router.addSubnet(subnet);
                        } else {
                            System.out.println("The router is not running!!");
                        }
                        break;
                    case "4":
                        ;
                        System.out.println("\nShowing the routes:");
                        if (isRunningRouter) {
                            System.out.println(this.router.showRoutes());
                        } else {
                            System.out.println("The router is not running!!");
                        }
                        break;
                    case "5":
                        if (isRunningRouter) {
                            this.router.openLogbook();
                        } else {
                            System.out.println("The router is not running!!");
                        }
                        break;
                    case "6":
                        System.out.println("\nSurely you want to exit the router? Y/N");
                        option = sc.nextLine();
                        option = option.toUpperCase();
                        while (!option.equals("Y") && !option.equals("N")) {
                            System.out.println("\nSurely you want to exit the router? Y/N");
                            option = sc.nextLine();
                            option = option.toUpperCase();
                        }
                        if (option.equals("Y")) {
                            contExecution = false;
                            System.out.println("\nExit router.");
                            System.exit(0);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
