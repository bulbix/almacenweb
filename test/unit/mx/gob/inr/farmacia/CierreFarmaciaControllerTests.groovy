package mx.gob.inr.farmacia



import org.junit.*
import grails.test.mixin.*

@TestFor(CierreFarmaciaController)
@Mock(CierreFarmacia)
class CierreFarmaciaControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/cierreFarmacia/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.cierreFarmaciaInstanceList.size() == 0
        assert model.cierreFarmaciaInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.cierreFarmaciaInstance != null
    }

    void testSave() {
        controller.save()

        assert model.cierreFarmaciaInstance != null
        assert view == '/cierreFarmacia/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/cierreFarmacia/show/1'
        assert controller.flash.message != null
        assert CierreFarmacia.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/cierreFarmacia/list'

        populateValidParams(params)
        def cierreFarmacia = new CierreFarmacia(params)

        assert cierreFarmacia.save() != null

        params.id = cierreFarmacia.id

        def model = controller.show()

        assert model.cierreFarmaciaInstance == cierreFarmacia
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/cierreFarmacia/list'

        populateValidParams(params)
        def cierreFarmacia = new CierreFarmacia(params)

        assert cierreFarmacia.save() != null

        params.id = cierreFarmacia.id

        def model = controller.edit()

        assert model.cierreFarmaciaInstance == cierreFarmacia
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/cierreFarmacia/list'

        response.reset()

        populateValidParams(params)
        def cierreFarmacia = new CierreFarmacia(params)

        assert cierreFarmacia.save() != null

        // test invalid parameters in update
        params.id = cierreFarmacia.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/cierreFarmacia/edit"
        assert model.cierreFarmaciaInstance != null

        cierreFarmacia.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/cierreFarmacia/show/$cierreFarmacia.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        cierreFarmacia.clearErrors()

        populateValidParams(params)
        params.id = cierreFarmacia.id
        params.version = -1
        controller.update()

        assert view == "/cierreFarmacia/edit"
        assert model.cierreFarmaciaInstance != null
        assert model.cierreFarmaciaInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/cierreFarmacia/list'

        response.reset()

        populateValidParams(params)
        def cierreFarmacia = new CierreFarmacia(params)

        assert cierreFarmacia.save() != null
        assert CierreFarmacia.count() == 1

        params.id = cierreFarmacia.id

        controller.delete()

        assert CierreFarmacia.count() == 0
        assert CierreFarmacia.get(cierreFarmacia.id) == null
        assert response.redirectedUrl == '/cierreFarmacia/list'
    }
}
