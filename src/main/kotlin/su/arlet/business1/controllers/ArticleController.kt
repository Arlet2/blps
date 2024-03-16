package su.arlet.business1.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.ArticleStatus
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.services.ArticleService

@RestController
@RequestMapping("\${api.path}/articles")
@Tag(name = "Article API")
class ArticleController(
    val articleService: ArticleService,
) {

    @GetMapping("/")
    @Operation(summary = "Get articles by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = Article::class)))
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getArticles(
        @RequestParam(name = "status", required = false) status: ArticleStatus?,
    ): ResponseEntity<*> {
        return try {
            val articles = articleService.getArticles(status)
            ResponseEntity(articles, HttpStatus.OK)
        } catch (e: Exception) {
            println("Error in get articles: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
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
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getArticleById(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity(articleService.getArticle(id), HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity("not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in get article by id: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new article")
    @ApiResponse(
        responseCode = "201", description = "Created article id", content = [
            Content(schema = Schema(implementation = Long::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createArticle(
        @RequestBody createArticleRequest: ArticleService.CreateArticleRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors())
            return ResponseEntity("Bad body: ${bindingResult.allErrors}", HttpStatus.BAD_REQUEST)

        return try {
            val articleId = articleService.addArticle(createArticleRequest)
            ResponseEntity(articleId, HttpStatus.CREATED)
        } catch (e: Exception) {
            println("Error in create article: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update article info")
    @ApiResponse(responseCode = "200", description = "Success - updated article")
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticle(
        @PathVariable id: Long,
        @RequestBody updatedArticle: ArticleService.UpdateArticleRequest,
    ): ResponseEntity<*> {
        return try {
            articleService.updateArticle(id, updatedArticle)
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in update article: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete article")
    @ApiResponse(responseCode = "200", description = "Success - deleted article")
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteArticle(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            articleService.deleteArticle(id)
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            println("Error in delete article: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update article status")
    @ApiResponse(responseCode = "200", description = "Updated article", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "Invalid status change", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticleStatus(
        @PathVariable id: Long,
        @RequestBody newStatus: ArticleStatus,
    ): ResponseEntity<*> {
        return try {
            articleService.updateArticleStatus(id, newStatus)
            ResponseEntity(null, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (_: UnsupportedOperationException) {
            ResponseEntity("Unsupported status change", HttpStatus.CONFLICT)
        } catch (e: Exception) {
            println("Error in update ad request: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}/ads")
    @Operation(summary = "Update article ads")
    @ApiResponse(responseCode = "200", description = "Updated article", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticleAds(
        @PathVariable id: Long,
        @RequestBody articleAdPostsIds: List<Long>,
    ): ResponseEntity<*> {
        return try {
            articleService.updateArticleAds(id, articleAdPostsIds)
            ResponseEntity(null, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in update ad request: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}