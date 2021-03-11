package salon.beauty.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visit")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id")
    private User users;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="service_id")
    private Services services;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="master_id")
    private Master master;

    private LocalDate date;
    private String time;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Visit(){
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public User getUser(){
        return users;
    }

    public void setUser(User user){
        this.users = user;
    }

    public Services getServices(){
        return services;
    }

    public void setServices(Services services){
        this.services = services;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public User getUsers(){
        return users;
    }

    public void setUsers(User users){
        this.users = users;
    }

    public Master getMaster(){
        return master;
    }

    public void setMaster(Master master){
        this.master = master;
    }
}