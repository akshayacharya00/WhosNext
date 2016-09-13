package appsshoppy.com.whosnext.model;

/**
 * Created by akshayacharya on 11/08/16.
 */
public class BusinessDashboard extends Services{
    private String clientName, clientService, serviceDate, serviceMonth, serviceTime, serviceStatus;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientService() {
        return clientService;
    }

    public void setClientService(String clientService) {
        this.clientService = clientService;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceMonth() {
        return serviceMonth;
    }

    public void setServiceMonth(String serviceMonth) {
        this.serviceMonth = serviceMonth;
    }

    @Override
    public String getServiceTime() {
        return serviceTime;
    }

    @Override
    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
