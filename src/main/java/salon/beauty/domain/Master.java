package salon.beauty.domain;

import javax.persistence.*;

@Entity
@Table(name = "master_time")
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")

    private User users;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String time;

    public Master(){
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

    public Category getCategory(){
        return category;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }
}