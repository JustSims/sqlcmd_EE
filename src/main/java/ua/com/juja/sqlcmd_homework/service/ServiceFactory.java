package ua.com.juja.sqlcmd_homework.service;

/**
 * Created by Sims on 23/11/2015.
 */
public class ServiceFactory {
    private Service service;

    public Service getService(){
        return service;
    }

    public void setService(Service service){
        this.service = service;
    }
}
