package ru.ac.uniyar

import org.flywaydb.core.api.FlywayException
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.ktorm.database.Database
import ru.ac.uniyar.authentication.JwtTools
import ru.ac.uniyar.authentication.Role
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.config.AppConfig
import ru.ac.uniyar.config.readConfiguration
import ru.ac.uniyar.db.H2DatabaseManager
import ru.ac.uniyar.db.connectToDatabase
import ru.ac.uniyar.db.performMigrations
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.filters.AuthenticationFilter
import ru.ac.uniyar.filters.ErrorFilter
import ru.ac.uniyar.handlers.authenticateUser
import ru.ac.uniyar.handlers.createNewAdvert
import ru.ac.uniyar.handlers.createNewCity
import ru.ac.uniyar.handlers.createNewRequest
import ru.ac.uniyar.handlers.createNewServiceCategory
import ru.ac.uniyar.handlers.editAdvert
import ru.ac.uniyar.handlers.editProfile
import ru.ac.uniyar.handlers.exitApp
import ru.ac.uniyar.handlers.redirectToStartPage
import ru.ac.uniyar.handlers.registerUser
import ru.ac.uniyar.handlers.setRequestStatus
import ru.ac.uniyar.handlers.showAdvert
import ru.ac.uniyar.handlers.showAdvertList
import ru.ac.uniyar.handlers.showCity
import ru.ac.uniyar.handlers.showCityList
import ru.ac.uniyar.handlers.showEditAdvertForm
import ru.ac.uniyar.handlers.showEditProfileForm
import ru.ac.uniyar.handlers.showLoginForm
import ru.ac.uniyar.handlers.showNewAdvertForm
import ru.ac.uniyar.handlers.showNewCityForm
import ru.ac.uniyar.handlers.showNewRequestForm
import ru.ac.uniyar.handlers.showNewServiceCategoryForm
import ru.ac.uniyar.handlers.showRegistrationForm
import ru.ac.uniyar.handlers.showRequest
import ru.ac.uniyar.handlers.showRequestList
import ru.ac.uniyar.handlers.showServiceCategoryList
import ru.ac.uniyar.handlers.showSpecialist
import ru.ac.uniyar.handlers.startPage
import ru.ac.uniyar.templates.ContextAwarePebbleTemplates
import ru.ac.uniyar.templates.ContextAwareViewRender
import kotlin.system.exitProcess

fun addAdmin(operationsHolder: OperationsHolder) {
    if (operationsHolder.getUserOperation.get("admin") == null)
        operationsHolder.addUserOperation
            .add(User(role = Role.ADMIN, login = "admin", password = "admin", name = "Администратор"))
}

fun app(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder, jwtTools: JwtTools): HttpHandler = routes(
    "/home" bind GET to startPage(view, operationsHolder),
    "/" bind GET to redirectToStartPage(),
    "/service_category/add" bind GET to showNewServiceCategoryForm(view),
    "/service_category/add" bind Method.POST to createNewServiceCategory(view, operationsHolder),
    "/service_category/list" bind GET to showServiceCategoryList(view, operationsHolder),
    "/city/add" bind GET to showNewCityForm(view),
    "/city/add" bind Method.POST to createNewCity(view, operationsHolder),
    "/city/list" bind GET to showCityList(view, operationsHolder),
    "/city/list/{id}" bind GET to showCity(view, operationsHolder),
//        "/specialist/add" bind GET to showNewSpecialistForm(view),
//        "/specialist/add" bind Method.POST to createNewSpecialist(view, operationsHolder),
//        "/specialist/list" bind GET to showSpecialistList(view, operationsHolder),
    "/specialist/list/{id}" bind GET to showSpecialist(view, operationsHolder),
    "/advert/add" bind GET to showNewAdvertForm(view, userLens, operationsHolder),
    "/advert/add" bind Method.POST to createNewAdvert(view, userLens, operationsHolder),
    "/advert/list" bind GET to showAdvertList(view, operationsHolder),
    "/advert/list/{id}" bind GET to showAdvert(view, operationsHolder),
    "advert/edit/{id}" bind GET to showEditAdvertForm(view, userLens, operationsHolder),
    "advert/edit/{id}" bind Method.POST to editAdvert(view, userLens, operationsHolder),
    "/request/add" bind GET to showNewRequestForm(view, userLens, operationsHolder),
    "/request/add" bind Method.POST to createNewRequest(view, userLens, operationsHolder),
    "/request/list" bind GET to showRequestList(view, userLens, operationsHolder),
    "/request/list/{id}" bind GET to showRequest(view, userLens, operationsHolder),
    "/request/list/{id}" bind Method.POST to setRequestStatus(view, userLens, operationsHolder),
    "/login" bind GET to showLoginForm(view),
    "/login" bind Method.POST to authenticateUser(view, operationsHolder, jwtTools),
    "/register" bind GET to showRegistrationForm(view, operationsHolder),
    "/register" bind Method.POST to registerUser(view, operationsHolder),
    "profile/edit" bind GET to showEditProfileForm(view, userLens, operationsHolder),
    "profile/edit" bind Method.POST to editProfile(view, userLens, operationsHolder),
    "/logout" bind GET to exitApp(),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

fun createWebServer(config: AppConfig): Http4kServer {
    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val operationsHolder = OperationsHolder(
        database = Database.connect(
            url = config.databaseConfig.dbServerHost,
            user = "sa",
            password = null
        ),
        salt = config.securityConfig.salt
    )
    addAdmin(operationsHolder)
    val jwtTools = JwtTools(config.securityConfig.salt, "uniyar", 7)
    val contexts = RequestContexts()
    val userLens: RequestContextLens<User?> = RequestContextKey.optional(contexts, "user")
    val htmlView = ContextAwareViewRender
        .withContentType(renderer, ContentType.TEXT_HTML)
        .associateContextLens("user", userLens)
    val errorFilter = ErrorFilter(htmlView).errorFilter
    val authenticationFilter = AuthenticationFilter(userLens, operationsHolder.getUserOperation::get, jwtTools).authenticationFilter
    val printingApp: HttpHandler = ServerFilters
        .InitialiseRequestContext(contexts)
        .then(authenticationFilter)
        .then(errorFilter)
        .then(app(htmlView, userLens, operationsHolder, jwtTools))

    return printingApp.asServer(Undertow(config.webConfig.webPort)).start()
}

fun main() {
    val config = readConfiguration()
    val h2databaseManager = H2DatabaseManager(config.databaseConfig.dbServerHost, config.databaseConfig.dbServerPort).initialize()
    try {
        performMigrations(config)
    } catch (e: FlywayException) {
        println("Не удалось выполнить миграцию: " + e.message)
        h2databaseManager.stopServers()
        exitProcess(-1)
    }
    connectToDatabase(config)
    val webServer = createWebServer(config)

    println("Сервер доступен по адресу http://localhost:" + webServer.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${config.databaseConfig.dbServerPort}")
    println("Введите любую строку, чтобы завершить работу приложения")

    readlnOrNull()

    webServer.stop()
    h2databaseManager.stopServers()
}
