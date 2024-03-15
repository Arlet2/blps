package su.arlet.business1.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.Article
import su.arlet.business1.services.ArticleService

@RestController
@RequestMapping("\${api.path}/articles")
@Tag(name = "Article API")
class ArticleController @Autowired constructor(
    val articleService: ArticleService,
) {

    @GetMapping("/")
    @Operation(summary = "Get articles by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = Article::class)))
        ]
    )
    fun getArticles(
        @RequestParam(name = "locationId", required = false) locationId: Long?,
        @RequestParam(name = "city", required = false) city: String?,
        @RequestParam(name = "district", required = false) district: String?,
        @RequestParam(name = "petTypeId", required = false) petTypeId: Long?,
        @RequestParam(name = "petType", required = false) petType: String?,
        @RequestParam(name = "petBreed", required = false) petBreed: String?,
        @RequestParam(name = "bloodTypeId", required = false) bloodTypeId: Long?,
        @RequestParam(name = "bloodType", required = false) bloodType: String?,
    ): ResponseEntity<*> {
        try {
            val articles = articleService.getArticles()
            return ResponseEntity(articles, HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("bad body", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get article by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found article", content = [
            Content(schema = Schema(implementation = Article::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    fun getArticleById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(null)
    }

    @PostMapping("/")
    @Operation(summary = "Create a new article")
    @ApiResponse(
        responseCode = "201", description = "Created", content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = Int::class)
            )
        ]
    )
    fun createArticle(
        @RequestBody petRequest: ArticleService.CreateArticleRequest,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        return ResponseEntity.ok(null)
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update article info")
    @ApiResponse(responseCode = "200", description = "Success - updated article")
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    fun updateArticle(
        @PathVariable id: Long,
        @RequestBody updatedPet: ArticleService.UpdateArticleRequest,
    ): ResponseEntity<*> {
        return ResponseEntity.ok(null)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete article")
    @ApiResponse(responseCode = "200", description = "Success - deleted article")
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    fun deleteArticle(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(null)
    }
}