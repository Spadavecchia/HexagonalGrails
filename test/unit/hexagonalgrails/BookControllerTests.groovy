package hexagonalgrails
import org.junit.*
import grails.test.mixin.*

@TestFor(BookController)
@Mock(Book)
class BookControllerTests {

    def populateValidParams(params) {
        assert params != null
        // Populate valid properties like...
        params["title"] = 'someValidName'
        params["author"] = new Author(name:"Cervantes")
        
    }

    void testIndex() {
        controller.index()
        assert "/book/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.instanceList.size() == 0
        assert model.instanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.instance != null
    }

    void testSave() {
        controller.save()

        assert model.instance != null
        assert view == '/book/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/book/show/1'
        assert controller.flash.message != null
        assert Book.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/book/list'

        populateValidParams(params)
        def book = new Book(params)

        assert book.save() != null

        params.id = book.id

        def model = controller.show()

        assert model.instance == book
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/book/list'

        populateValidParams(params)
        def book = new Book(params)

        assert book.save() != null

        params.id = book.id

        def model = controller.edit()

        assert model.instance == book
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/book/list'

        response.reset()

        populateValidParams(params)
        def book = new Book(params)

        assert book.save() != null

        // test invalid parameters in update
        params.id = book.id
        //add invalid values to params object
        String invalidTitle = "a"
        params.title = invalidTitle

        controller.update()

        assert view == "/book/edit"
        assert model.instance != null

        book.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/book/show/$book.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        book.clearErrors()

        populateValidParams(params)
        params.id = book.id
        params.version = -1
        controller.update()

        assert view == "/book/edit"
        assert model.instance != null
        assert model.instance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/book/list'

        response.reset()

        populateValidParams(params)
        def book = new Book(params)

        assert book.save() != null
        assert Book.count() == 1

        params.id = book.id

        controller.delete()

        assert Book.count() == 0
        assert Book.get(book.id) == null
        assert response.redirectedUrl == '/book/list'
    }
}
