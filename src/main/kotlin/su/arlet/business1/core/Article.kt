package su.arlet.business1.core

import jakarta.persistence.*

@Entity
@Table(name = "articles")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val title: String,
    val text: String,
    val images: Map<String, String> // todo: ?
)