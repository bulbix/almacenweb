package mx.gob.inr.ceye



import org.junit.*
import grails.test.mixin.*

@TestFor(ArticuloCeyeController)
@Mock(ArticuloCeye)
class ArticuloCeyeControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/articuloCeye/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.articuloCeyeInstanceList.size() == 0
        assert model.articuloCeyeInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.articuloCeyeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.articuloCeyeInstance != null
        assert view == '/articuloCeye/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/articuloCeye/show/1'
        assert controller.flash.message != null
        assert ArticuloCeye.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/articuloCeye/list'

        populateValidParams(params)
        def articuloCeye = new ArticuloCeye(params)

        assert articuloCeye.save() != null

        params.id = articuloCeye.id

        def model = controller.show()

        assert model.articuloCeyeInstance == articuloCeye
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/articuloCeye/list'

        populateValidParams(params)
        def articuloCeye = new ArticuloCeye(params)

        assert articuloCeye.save() != null

        params.id = articuloCeye.id

        def model = controller.edit()

        assert model.articuloCeyeInstance == articuloCeye
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/articuloCeye/list'

        response.reset()

        populateValidParams(params)
        def articuloCeye = new ArticuloCeye(params)

        assert articuloCeye.save() != null

        // test invalid parameters in update
        params.id = articuloCeye.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/articuloCeye/edit"
        assert model.articuloCeyeInstance != null

        articuloCeye.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/articuloCeye/show/$articuloCeye.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        articuloCeye.clearErrors()

        populateValidParams(params)
        params.id = articuloCeye.id
        params.version = -1
        controller.update()

        assert view == "/articuloCeye/edit"
        assert model.articuloCeyeInstance != null
        assert model.articuloCeyeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/articuloCeye/list'

        response.reset()

        populateValidParams(params)
        def articuloCeye = new ArticuloCeye(params)

        assert articuloCeye.save() != null
        assert ArticuloCeye.count() == 1

        params.id = articuloCeye.id

        controller.delete()

        assert ArticuloCeye.count() == 0
        assert ArticuloCeye.get(articuloCeye.id) == null
        assert response.redirectedUrl == '/articuloCeye/list'
    }
}
