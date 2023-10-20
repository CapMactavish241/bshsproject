// Computer Project 2023-24; Personal Money Management Application
// By Ashutosh Mishra, XB, Roll 3

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Main {
    static Income income = new Income();
    static Scanner sc = new Scanner(System.in);
    static File fl = new File("userinfo");

    public static void main(String[] args) throws IOException {
        fl.mkdir();
        fl = new File(fl.getPath() + "/income.dat");
        mainMenu();
    }

    static void mainMenu() throws IOException {
        int result = menu("Personal Money Management Application", new String[]{"Exit", "Set monthly income", "View monthly Income", "Edit Income Sources", "Delete user data"});
        switch (result) {
            case 0 -> {
                System.out.println("Gracefully Shutting Down");
                System.exit(0);
            }
            case 1 -> {
                income.setIncome();
                mainMenu();
            }
            case 2 -> {
                income.viewEntry();
                mainMenu();
            }
            case 3 -> {
                income.edit();
                mainMenu();
            }
            case 4 -> {
                System.out.print("Deleting user data");
                File folder = new File("userinfo");
                income = new Income();
                if (folder.listFiles() != null) {
                    for (File toDelete : folder.listFiles()) {
                        DataOutputStream dos = new DataOutputStream(new FileOutputStream(toDelete));
                        dos.close();
                        toDelete.delete();
                    }
                }
                folder.delete();
                mainMenu();
            }
        }
    }

    static void printOption(int selNumber, String option) {
        System.out.println("[" + selNumber + "] " + option);
    }

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n================ " + header + " ================");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again or press Ctrl+D to exit");
            boolean hasExitFirst = options[0].equalsIgnoreCase("Exit");
            for (int i = 0; i < options.length; i++) {
                if (hasExitFirst && i == 0)
                    continue;
                printOption(i, options[i]);
            }
            if (hasExitFirst)
                printOption(0, options[0]);
            System.out.print("Select an option to proceed:");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        System.out.println("=".repeat(("================ " + header + " ================").length()));
        return choice;
    }

    static void fileReplacer(File org, File dest) throws IOException {
        dest.createNewFile();
        org.createNewFile();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(dest));
        DataInputStream dis = new DataInputStream(new FileInputStream(org));
        try {
            while (true) {
                dos.writeUTF(dis.readUTF());
                dos.writeDouble(dis.readDouble());
            }
        } catch (EOFException ignored) {
        }
        dos.close();
        dis.close();
    }
}

class Income {
    String[] sources;
    Scanner sc = new Scanner(System.in);
    double[] income;

    void writeFile(boolean append) throws IOException {
        File fl = new File("userinfo/income.dat");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl, append));
        sources = new String[1];
        char ch;
        do {
            System.out.println("Enter your source of income:");
            sources[sources.length - 1] = new Scanner(System.in).nextLine();
            System.out.println("Would you like to add another one?(Y/N)");
            ch = new Scanner(System.in).next().charAt(0);
            if (ch == 'y' || ch == 'Y') sources = arrayExpander(sources);
        } while (ch == 'Y' || ch == 'y');
        income = new double[sources.length];
        for (String source : sources) {
            dos.writeUTF(source);
            dos.writeDouble(0.0);
        }
        dos.close();
        readFile();
    }

    String[] arrayExpander(String[] array) {
        String[] temparray = array;
        array = new String[temparray.length + 1];
        System.arraycopy(temparray, 0, array, 0, temparray.length);
        return array;
    }

    void readFile() throws IOException {
        File fl = new File("userinfo/income.dat");
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        int l = 0;
        try {
            while (true) {
                dis.readUTF();
                dis.readDouble();
                l++;
            }
        } catch (EOFException ignored) {
        }
        sources = new String[l];
        income = new double[l];
        dis.close();
        dis = new DataInputStream(new FileInputStream(fl));
        for (int i = 0; i < sources.length; i++) {
            sources[i] = dis.readUTF();
            income[i] = dis.readDouble();
        }
        dis.close();
    }

    void createReadFile() throws IOException {
        File fl = new File("userinfo/income.dat");
        try {
            fl.createNewFile();
        } catch (IOException ignored) {
        }
        boolean isEmpty = new BufferedReader(new FileReader(fl.getPath())).readLine() == null;
        if (isEmpty) {
            System.out.println("WARNING: No data found in file, creating a new file, ignore if this is first run\n");
            writeFile(false);
        } else {
            readFile();
        }
    }

    void setIncome() throws IOException {
        createReadFile();
        File fl = Main.fl;
        File tmp = new File("userinfo/income.tmp");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(tmp));
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        int sel = Main.menu("Select income source:", sources);
        System.out.println("Enter amount of income to add:");
        double amt = sc.nextDouble();
        income[sel] = amt;
        try {
            int record = 0;
            while (true) {
                dos.writeUTF(dis.readUTF());
                if (record == sel) {
                    dos.writeDouble(income[sel]);
                    dis.readDouble();
                } else dos.writeDouble(dis.readDouble());
                record++;
            }
        } catch (EOFException ignored) {
        }
        dos.close();
        dis.close();
        Main.fileReplacer(tmp, fl);
        tmp.delete();
    }

    void addEntry() throws IOException {
        writeFile(true);
    }

    void viewEntry() throws IOException {
        createReadFile();
        double sum = 0;
        for (double s : income) sum += s;
        System.out.println("Total Monthly Income = " + sum);
        System.out.print("Would you like to view monthly income gained from a specific source? (Y/N)");
        char ch;
        do {
            ch = sc.next().charAt(0);
        } while (ch != 'Y' && ch != 'z' && ch != 'Z' && ch != 'y');
        if (ch == 'Y' || ch == 'y') {
            int sel = Main.menu("Select income source to view", sources);
            System.out.println("Income gained per month by " + sources[sel] + " = " + income[sel]);
        }
    }

    void edit() throws IOException {
        createReadFile();
        int option = Main.menu("Select action", new String[]{"Exit", "Add Income Source", "List Income Sources", "Delete Income Source"});
        switch (option) {
            case 0 -> Main.mainMenu();
            case 1 -> {
                addEntry();
                edit();
            }
            case 2 -> {
                list();
                edit();
            }
            case 3 -> {
                int sel = Main.menu("Which entry do you want to delete?", sources);
                File tmp = new File("userinfo/income.tmp");
                File fl = new File("userinfo/income.dat");
                Main.fileReplacer(fl, tmp);
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
                DataInputStream dis = new DataInputStream(new FileInputStream(tmp));
                try {
                    for (int c = 0; true; c++) {
                        if (c != sel) {
                            dos.writeUTF(dis.readUTF());
                            dos.writeDouble(dis.readDouble());
                        } else {
                            dis.readUTF();
                            dis.readDouble();
                        }
                    }
                } catch (EOFException ignored) {
                }
                dos.close();
                dis.close();
                tmp.delete();
                readFile();
                edit();
            }
        }
    }

    void list() throws IOException {
        createReadFile();
        for (int i = 0; i < sources.length; i++) {
            System.out.println("[" + i + "]" + sources[i]);
        }
    }
}