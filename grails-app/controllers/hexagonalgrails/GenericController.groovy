package hexagonalgrails

import org.springframework.dao.DataIntegrityViolationException

class GenericController {
    def domainName
    def baseViews
    
    def savingError(instance){
        render(view: "create", model: [instance: instance])
    }
    def savingOk(instance){
        flash.message = message(code: 'default.created.message', args: [message(code: '${domainName}.label'), instance.id])
        redirect(action: "show", id: instance.id)
    }

    def showingError(Long id){
        flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainName}.label', default: '${domainName}'), id])
        redirect(action: "list")       
    }

    def editingError(Long id){
            flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainName}.label', default: '${domainName}'), id])
            redirect(action: "list")
            return        
    }

    def notFound(instance){
        flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainName}.label', default: '${domainName}'), instance?.id])
        redirect(action: "list")
    }
    def updatingOk(instance){
        flash.message = message(code: 'default.updated.message', args: [message(code: '${domainName}.label', default: '${domainName}'), instance.id])
        redirect(action: "show", id: instance.id)
    }
    def updatingError(instance){
        render(view: "edit", model: [instance: instance])
    }
    def updatingVersionError(instance){
        instance.errors.rejectValue("version", "default.optimistic.locking.failure",
                  [message(code: '${domainName}.label', default: '${domainName}')] as Object[],
                  "Another user has updated this ${domainName} while you were editing")
        render(view: "edit", model: [instance: instance])
        return  
    }
    def deletingOk(instance){
        flash.message = message(code: 'default.deleted.message', args: [message(code: '${domainName}.label', default: '${domainName}'), instance.id])
        redirect(action: "list")
    }
    def deletingError(instance){
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: '${domainName}.label', default: '${domainName}'), instance.id])
            redirect(action: "show", id: id)
    }
}
