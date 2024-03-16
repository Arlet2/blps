package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import su.arlet.business1.core.enums.ArticleStatus

@Entity
@Table(name = "articles")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @NotEmpty
    var title: String,

    @NotEmpty
    var text: String,

    @ManyToMany
    @JoinTable(name = "article_images")
    var images: List<Image>,

    @ManyToMany
    @JoinTable(name = "article_ad_posts")
    var adPosts: List<AdPost> = emptyList(),

    var status: ArticleStatus,
)