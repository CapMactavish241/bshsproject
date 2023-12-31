import java.util.*;
import java.io.*;

class Transaction {
    private String description;
    private boolean isImportant;
    private double amount;
    private Date timeCreated;

    public Transaction(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.timeCreated = new Date(System.currentTimeMillis());
        isImportant = false;
    }

    public Date getTime() {
        return timeCreated;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}

class PersonalFinanceManager {
    private double balance;
    private Transaction[] transactions;
    public String currencySymbol;

    public PersonalFinanceManager(Currency currency) {
        balance = 0.0;
        transactions = new Transaction[0];
        currencySymbol = currency.getCurrencyCode();
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getBalance() {
        balance = 0.0;
        for (Transaction transaction : transactions)
            balance += transaction.getAmount();
        return balance;
    }

    public void addTransaction(String description, double amount) {
        Transaction transaction = new Transaction(description, amount);
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
        System.out.println("Added transaction with ID: " + (transactions.length - 1));
    }

    public void addTransaction(Transaction transaction) {
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
        System.out.println("Added transaction with ID: " + (transactions.length - 1));
    }

    public void add(Transaction transaction) {
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
    }

    public void remove(int index) {
        Transaction[] tempArray = new Transaction[transactions.length - 1];
        int oldIndex = 0;
        for (int i = 0; i < transactions.length; i++)
            if (i != index) tempArray[oldIndex++] = transactions[i];
        transactions = tempArray;
    }

    public void displayTransactions() {
        String header = "";
        for (int i = 0; i < 28; i++)
            header = header.concat("-");
        header += " Transaction History ";
        for (int i = 0; i < 28; i++)
            header = header.concat("-");
        System.out.println(header);
        String[][] rows = new String[transactions.length + 1][5];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")", "Important"};
        for (int i = 0; i < transactions.length; i++)
            displayTransaction(rows, i, true);
        System.out.println(Main.formatAsTable(rows));
        for (int i = 0; i < header.length(); i++) System.out.print("-");
        System.out.println();
    }

    public void displayImportant() {
        String header = "";
        for (int i = 0; i < 28; i++)
            header = header.concat("-");
        header += " Important Transaction History ";
        for (int i = 0; i < 28; i++)
            header = header.concat("-");
        System.out.println(header);
        int l = 0;
        for (Transaction transaction : transactions)
            if (transaction.isImportant())
                l++;
        String[][] rows = new String[l + 1][4];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")"};
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i].isImportant()) {
                displayTransaction(rows, i, false);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        for (int i = 0; i < header.length(); i++) System.out.print("-");
        System.out.println();
    }

    public void displayOptional() {
        String header = "";
        for (int i = 0; i < 28; i++) header = header.concat("-");
        header += " Optional Transaction History ";
        for (int i = 0; i < 28; i++) header = header.concat("-");
        System.out.println(header);
        int l = 0;
        for (Transaction transaction : transactions) if (!(transaction.isImportant())) l++;
        String[][] rows = new String[l + 1][4];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")"};
        for (int i = 0; i < transactions.length; i++) {
            if (!(transactions[i].isImportant())) {
                displayTransaction(rows, i, false);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        for (int i = 0; i < header.length(); i++) System.out.print("-");
        System.out.println();
    }

    private void displayTransaction(String[][] rows, int i, boolean showImportant) {
        Transaction transaction = transactions[i];
        String id = i + "";
        String dateCreated = Main.parseDateToString(transaction.getTime()).split(" ")[0];
        String timeCreated = Main.parseDateToString(transaction.getTime()).split(" ")[1];
        String description = transaction.getDescription();
        String amount = transaction.getAmount() + "";
        if (showImportant) {
            boolean important = transaction.isImportant();
            rows[i + 1] = new String[]{id, dateCreated, timeCreated, description, amount, important ? "*" : " "};
        } else rows[i + 1] = new String[]{id, dateCreated, timeCreated, description, amount};
    }

    public void displayByDate(String date) {
        String header = "";
        for (int i = 0; i < 28; i++) header = header.concat("-");
        header += " Transactions on " + date;
        for (int i = 0; i < 28; i++) header = header.concat("-");
        System.out.println(header);
        int l = 0;
        for (Transaction transaction : transactions) {
            if (date.equalsIgnoreCase(Main.parseDateToString(transaction.getTime()).split(" ")[0])) l++;
        }
        String[][] rows = new String[l + 1][5];
        String[] headers = {"ID", "Time", "Description", "Amount(" + currencySymbol + ")", "Important"};
        rows[0] = headers;
        int rowCount = 1;
        for (int i = 0; i < transactions.length; i++) {
            if (date.equalsIgnoreCase(Main.parseDateToString(transactions[i].getTime()).split(" ")[0])) {
                Transaction transaction = transactions[i];
                String id = String.valueOf(i);
                String timeCreated = Main.parseDateToString(transaction.getTime()).split(" ")[1];
                String description = transaction.getDescription();
                String amount = transaction.getAmount() + "";
                String important = transaction.isImportant() ? "*" : " ";
                rows[rowCount] = new String[]{id, timeCreated, description, amount, important};
                rowCount++;
            }
        }
        System.out.println(Main.formatAsTable(rows));
        for (int i = 0; i < header.length(); i++) System.out.print("-");
        System.out.println();
    }


    public boolean deleteTransaction(String description) {
        boolean deleted = false;
        int delCount = 0;
        for (Transaction transaction : transactions)
            if (transaction.getDescription().equalsIgnoreCase(description)) delCount++;
        for (int c = 0; c < delCount; c++) {
            for (int i = 0; i < transactions.length; i++) {
                if (transactions[i].getDescription().equalsIgnoreCase(description)) {
                    deleted = true;
                    remove(i);
                }
            }
        }
        return deleted;
    }

    public double totalIncome() {
        double income = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() >= 0) income += transaction.getAmount();
        }
        return income;
    }

    public double totalExpenses() {
        double expense = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() <= 0) expense += Math.abs(transaction.getAmount());
        }
        return expense;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Transaction transaction : transactions) {
            dos.writeUTF("Transaction:");
            dos.writeUTF(Main.parseDateToString(transaction.getTime()));
            dos.writeUTF(transaction.getDescription());
            dos.writeDouble(transaction.getAmount());
            dos.writeBoolean(transaction.isImportant());
        }
        dos.close();
    }

    public void read(File fl) throws IOException {
        boolean emptyFile = fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        transactions = new Transaction[0];
        if (!emptyFile) {
            while (true) {
                try {
                    if (dis.readUTF().equals("Transaction:")) {
                        String time = dis.readUTF();
                        String description = dis.readUTF();
                        double amount = dis.readDouble();
                        boolean important = dis.readBoolean();
                        Transaction transaction = new Transaction(description, amount);
                        transaction.setTime(Main.parseDate(time));
                        transaction.setImportant(important);
                        add(transaction);
                    }
                } catch (EOFException ignored) {
                    break;
                }
            }
        }
        dis.close();
    }
}

class FinancialCalculators {
    private final Scanner input;

    public FinancialCalculators(Scanner input) {
        this.input = input;
    }

    public Transaction recurringDeposit() {
        System.out.print("Enter amount paid per deposit: ");
        double ed = input.nextDouble();
        System.out.print("Enter rate of interest: ");
        double r = input.nextDouble();
        System.out.println("How many months would this recurring deposit last?");
        int n = input.nextInt();
        double simpleInterest = (ed * n * (n + 1) * r) / (2d * 12 * 100);
        double maturityValue = ed * n + simpleInterest;
        System.out.println("Maturity value of recurring deposit: " + maturityValue);
        return new Transaction("Recurring Deposit", maturityValue);
    }

    public Transaction simpleInterest() {
        System.out.print("Enter amount paid: ");
        double ed = input.nextDouble();
        System.out.print("Enter rate of interest: ");
        double r = input.nextDouble();
        System.out.println("How many months would this account last?");
        int n = input.nextInt();
        double simpleInterest = (ed * r * n) / (12d * 100);
        System.out.println("Interest earned on account: " + simpleInterest);
        return new Transaction("Simple Interest", simpleInterest);
    }
}

class Main {
    static File fl;
    static File folder;
    static File cfl;
    static Scanner sc;
    static FinancialCalculators fc;
    static PersonalFinanceManager manager;
    static Currency currency;

    public static void main(String[] args) {
        folder = new File("userinfo");
        fl = new File("userinfo/transactionHistory.log");
        cfl = new File("userinfo/user.currency");
        if (args.length > 0 && args[0].equalsIgnoreCase("--rem")) {
            boolean deleted = fl.delete() && cfl.delete();
            System.out.println(deleted ? "All files deleted successfully" : "Some files couldn't be deleted, these may not have existed");
            System.exit(0);
        }
        if (!folder.exists() && folder.mkdirs()) System.out.println("Welcome, user");
        sc = new Scanner(System.in);
        fc = new FinancialCalculators(sc);
        try {
            currency = setCurrency();
        } catch (IOException ignored) {
        }
        manager = new PersonalFinanceManager(currency);
        mainMenu();
    }

    static Currency setCurrency() throws IOException {
        String cr;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(cfl));
            cr = dis.readUTF();
            dis.close();
        } catch (IOException i) {
            System.out.println("Enter your currency(for example, INR for Indian Rupees): ");
            cr = setCurrency(sc.next()).getCurrencyCode();
        }
        return Currency.getInstance(cr);
    }

    static Currency setCurrency(String cr) throws IOException {
        try {
            cr = cr.substring(0, 3);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Invalid data, setting currency to INR");
            cr = "INR";
        }
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(cfl));
        try {
            Currency.getInstance(cr);
        } catch (IllegalArgumentException i) {
            System.out.println("Invalid data, setting currency to INR");
            cr = "INR";
        }
        dos.writeUTF(cr);
        dos.close();
        return Currency.getInstance(cr);
    }

    static void mainMenu() {
        while (true) {
            try {
                manager.read(fl);
            } catch (IOException e) {
                System.out.println("ERROR: Couldn't read data file");
            }
            int choice = menu("\033[1mPersonal Money Management Application\033[0m", getMainMenuOptions());
            switch (choice) {
                case 1: {
                    System.out.print("Enter income description: ");
                    String incomeDescription = sc.nextLine();
                    System.out.print("Enter income amount: " + manager.currencySymbol + " ");
                    double incomeAmount = sc.nextDouble();
                    manager.addTransaction(incomeDescription, incomeAmount);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 2: {
                    System.out.print("Enter expense description: ");
                    String expenseDescription = sc.nextLine();
                    System.out.print("Enter expense amount: " + manager.currencySymbol + " ");
                    double expenseAmount = sc.nextDouble();
                    manager.addTransaction(expenseDescription, -expenseAmount);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter transaction ID to mark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions()[id].setImportant(true);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter transaction ID to unmark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions()[id].setImportant(false);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 5: {
                    System.out.print("Enter transaction ID to delete: ");
                    int toDelete = sc.nextInt();
                    if (toDelete < manager.getTransactions().length && toDelete >= 0) {
                        manager.remove(toDelete);
                        System.out.println("Transaction deleted successfully");
                    } else System.out.println("Transaction with ID " + toDelete + " not found");
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 6: {
                    System.out.print("Enter transaction description to delete: ");
                    String toDelete = sc.nextLine();
                    if (manager.deleteTransaction(toDelete)) System.out.println("Transaction deleted successfully");
                    else System.out.println("No such transaction found");
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 7: {
                    System.out.print("Enter transaction ID to edit: ");
                    int toEdit = sc.nextInt();
                    boolean exists = manager.getTransactions().length > toEdit;
                    String[] menuOptions;
                    if (exists) {
                        menuOptions = new String[]{"Go Back", "Description", "Amount", "Time of Creation"};
                        switch (menu("What would you like to edit?", menuOptions)) {
                            case 1: {
                                System.out.print("Enter new description for transaction: ");
                                String str = sc.nextLine();
                                manager.getTransactions()[toEdit].setDescription(str);
                                break;
                            }
                            case 2: {
                                System.out.print("Enter updated amount for transaction: ");
                                double newAmount = sc.nextDouble();
                                manager.getTransactions()[toEdit].setAmount(newAmount);
                                break;
                            }
                            case 3: {
                                System.out.print("Enter new date and time(format yyyy/MM/dd HH:mm:ss): ");
                                String inputDateTime = sc.nextLine();
                                manager.getTransactions()[toEdit].setTime(parseDate(inputDateTime));
                                break;
                            }
                        }
                    } else {
                        System.out.println("No such transaction found...");
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
                case 8:
                    System.out.println("Total income recorded: " + manager.totalIncome());
                    break;
                case 9:
                    System.out.println("Total expenditure recorded: " + manager.totalExpenses());
                    break;
                case 10: {
                    System.out.println("Current Balance: " + manager.currencySymbol + " " + manager.getBalance());
                    System.out.print("Press Enter to continue....");
                    sc.nextLine();
                    break;
                }
                case 11: {
                    manager.displayImportant();
                    System.out.print("Press Enter to continue....");
                    sc.nextLine();
                    break;
                }
                case 12: {
                    manager.displayOptional();
                    System.out.print("Press Enter to continue....");
                    sc.nextLine();
                    break;
                }
                case 13: {
                    manager.displayTransactions();
                    System.out.print("Press Enter to continue....");
                    sc.nextLine();
                    break;
                }
                case 14: {
                    System.out.println("Enter date to filter(yyyy/MM/dd): ");
                    String date = sc.nextLine();
                    manager.displayByDate(date);
                    System.out.print("Press Enter to continue....");
                    sc.nextLine();
                    break;
                }
                case 15: {
                    Transaction tr = fc.recurringDeposit();
                    char ch;
                    System.out.println("Would you like to add the maturity value as an income transaction?(Y/N)");
                    ch = sc.next().charAt(0);
                    if (ch == 'Y' || ch == 'y') manager.addTransaction(tr);
                    break;
                }
                case 16: {
                    Transaction tr = fc.simpleInterest();
                    char ch;
                    System.out.println("Would you like to add the interest as an income transaction?(Y/N)");
                    ch = sc.next().charAt(0);
                    if (ch == 'Y' || ch == 'y') manager.addTransaction(tr);
                    break;
                }

                case 17: {
                    try {
                        new DataOutputStream(new FileOutputStream(cfl)).close();
                    } catch (IOException ignored) {
                    }
                    System.out.print("Enter new currency code(for example, USD for United States Dollar): ");
                    try {
                        currency = setCurrency(sc.next());
                    } catch (IOException e) {
                        System.out.println("Couldn't save currency data");
                    }
                    manager.setCurrencySymbol(currency.getCurrencyCode());
                    System.out.println("Changed Currency Symbol To: " + currency.getCurrencyCode());
                    break;
                }

                case 18: {
                    System.out.print("Deleting all user data: ");
                    System.out.println(fl.delete() ? "Successful" : "Unsuccessful");
                    manager = new PersonalFinanceManager(currency);
                    break;
                }
                default: {
                    System.out.println("Exiting...");
                    System.exit(0);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    break;
                }
            }
        }
    }

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        String headerFull = "                  * " + header + " *                  ";
        for (int i = 0; i < headerFull.length(); i++) System.out.print("-");
        System.out.println();
        System.out.println(headerFull);
        for (int i = 0; i < headerFull.length(); i++) System.out.print("-");
        System.out.println();
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again");
            boolean hasExitOrGoBack = options[0].equalsIgnoreCase("Exit") || options[0].equalsIgnoreCase("Go Back");
            for (int i = 0; i < options.length; i++) {
                if (hasExitOrGoBack && i == 0) continue;
                printOptions(headerFull.length(), i, options[i]);
            }
            System.out.println();
            if (hasExitOrGoBack)
                printOptions(headerFull.length(), 0, options[0]);
            System.out.println();
            System.out.print("Select an option to proceed: ");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        for (int i = 0; i < headerFull.length(); i++) System.out.print("=");
        System.out.println();
        sc.nextLine();
        return choice;
    }

    public static String formatAsTable(String[][] rows) {
        int columnCount = rows[0].length;
        int[] maxLengths = new int[columnCount];
        for (String[] strings : rows) {
            for (int col = 0; col < columnCount; col++) {
                maxLengths[col] = Math.max(maxLengths[col], strings[col].length());
            }
        }
        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();
        StringBuilder result = new StringBuilder();
        for (String[] strings : rows) {
            result.append(String.format(format, (Object[]) strings)).append("\n");
        }
        return result.toString();

    }

    static void printOptions(int headerLength, int opNumber, String option) {
        String space = "  ";
        if (opNumber > 9) space = " ";
        for (int i = 0; i < headerLength / 6; i++) System.out.print(" ");
        System.out.println(opNumber + "." + space + option);
    }

    public static String[] getMainMenuOptions() {
        String[] options = new String[19];
        options[0] = "Exit";
        options[1] = "Add Income";
        options[2] = "Add Expense";
        options[3] = "Mark Transaction Important";
        options[4] = "Unmark Transaction Important";
        options[5] = "Delete Transaction by ID";
        options[6] = "Delete Transactions by Description";
        options[7] = "Edit Transaction";
        options[8] = "View Total Income";
        options[9] = "View Total Expenditure";
        options[10] = "View Balance";
        options[11] = "View Important Transactions";
        options[12] = "View Optional Transactions";
        options[13] = "View Transaction History";
        options[14] = "View Transactions By Date";
        options[15] = "Calculate Maturity Value Of Recurring Deposit";
        options[16] = "Calculate Simple Interest";
        options[17] = "Change Display Currency";
        options[18] = "Delete All Transaction Records";
        return options;
    }

    public static Date parseDate(String input) {
        String date = input.split(" ")[0];
        String time = input.split(" ")[1];
        int years = Integer.parseInt(date.split("/")[0]);
        int months = Integer.parseInt(date.split("/")[1]) - 1;
        int days = Integer.parseInt(date.split("/")[2]);
        int hours = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);
        int seconds = Integer.parseInt(time.split(":")[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(years, months, days, hours, minutes, seconds);
        return calendar.getTime();
    }

    public static String parseDateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String str = "";
        str += calendar.get(Calendar.YEAR) + "/";
        if (calendar.get(Calendar.MONTH) <= 9) str += "0";
        str += (calendar.get(Calendar.MONTH) + 1) + "/";
        if (calendar.get(Calendar.DAY_OF_MONTH) <= 9) str += "0";
        str += calendar.get(Calendar.DAY_OF_MONTH) + " ";
        if (calendar.get(Calendar.HOUR_OF_DAY) <= 9) str += "0";
        str += calendar.get(Calendar.HOUR_OF_DAY) + ":";
        if (calendar.get(Calendar.MINUTE) <= 9) str += "0";
        str += calendar.get(Calendar.MINUTE) + ":";
        if (calendar.get(Calendar.SECOND) <= 9) str += "0";
        str += calendar.get(Calendar.SECOND);
        return str;
    }
}