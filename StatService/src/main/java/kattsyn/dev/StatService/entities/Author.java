package kattsyn.dev.StatService.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "authors")
@NoArgsConstructor
public class Author {
    @Id
    private Long id;
    private int likes;
    private int views;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;


    public Author(Long id) {
        this.id = id;
        this.likes = 0;
        this.views = 0;
        this.posts = new ArrayList<>();
    }

    public Author(Long id, Post post) {
        this.id = id;
        this.likes = 0;
        this.views = 0;
        this.posts = new ArrayList<>();
        this.posts.add(post);
    }

    public void increaseLikes(int amount) {
        this.likes += amount;
    }

    public void increaseViews(int amount) {
        this.likes += amount;
    }
}
