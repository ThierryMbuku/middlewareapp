package prosense.sassa.niisclient;

public class Test {

    public static void main(String args[]) throws Exception {
        NIISBankService service = new NIISBankService();
        NIISBankServiceSoap port = service.getNIISBankServiceSoap();

        try {
            NIISResults response = port.queryNIIS("PTASOM001170815", null);
            System.out.println("NIISFileNumber :: " + response.getResults().getNIISDetails().get(0).getNIISFileNumber());
            System.out.println("FirstName :: " + response.getResults().getNIISDetails().get(0).getFirstName());
            System.out.println("Status :: " + response.getResults().getNIISDetails().get(0).getStatus());
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
