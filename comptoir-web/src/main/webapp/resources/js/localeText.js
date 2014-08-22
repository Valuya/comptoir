/* 
 * Valuya sprl 2014
 */
function getWidgetVarById(id) {
    for (var propertyName in PrimeFaces.widgets) {
        if (PrimeFaces.widgets[propertyName].id === id) {
            return PrimeFaces.widgets[propertyName];
        }
    }
}

function handleLangSelection(selectId) {
    var selectWidgetVar = getWidgetVarById(selectId);
    var lang = selectWidgetVar.getSelectedValue();

    selectLang(lang);
}

var reentrant = false;

function selectLang(lang) {
    if (!reentrant) {
        reentrant = true;

        $('.localeText').hide();
        $('.localeText.' + lang).show();

        localeSelects = $('.localeSelect');
        for (var i = 0; i < localeSelects.length; i++) {
            var localeSelect = localeSelects[i];
            var selectWidgetVar = getWidgetVarById(localeSelect.id);
//            try {
                selectWidgetVar.selectValue(lang);
//            } catch (err) {
//                
//            }
        }
        reentrant = false;
    }
}