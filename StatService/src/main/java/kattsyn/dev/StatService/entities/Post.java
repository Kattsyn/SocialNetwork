package kattsyn.dev.StatService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private Long id;
    private int likes;
    private int views;

    @ManyToOne
    private Author author;

    public void increaseLikes(int amount) {
        this.likes += amount;
    }

    public void increaseViews(int amount) {
        this.views += amount;
    }
}
