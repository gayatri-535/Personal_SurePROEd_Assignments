public class Customer {

    private int customerId;
    private String name;
    private String phone;
    private String password;

    public Customer(int customerId, String name, String phone, String password) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean checkPassword(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    public String toFileString() {
        return customerId + "|" + name + "|" + phone + "|" + password;
    }

    public void displayCustomerDetails() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
    }
}