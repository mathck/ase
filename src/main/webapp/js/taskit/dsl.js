function configureDSL($scope, $stateParams, ErrorHandler, TemplateFactory){
    $scope.tID=$stateParams.tID;

    $scope.template={};
    TemplateFactory.show({tID: $scope.tID}).$promise.then(function(response){
        $scope.template=response;
        var identifier;
        var body;

        $scope.title = "";
        parser = new DOMParser();
        $scope.syntax=$scope.template.syntax;
        xmlDoc = parser.parseFromString($scope.template.syntax,"text/xml");
        $scope.identifier = xmlDoc.getElementsByTagName("identifier")[0].childNodes[1].nodeValue;
        identifier = xmlDoc.getElementsByTagName("identifier")[0].childNodes;

        for (i = 0; i < identifier.length; i++) {
            switch(identifier[i].nodeName) {
                case "title":
                    $scope.title = identifier[i].childNodes[0].nodeValue;
                    break;
                case "description":
                    $scope.description = identifier[i].childNodes[0].nodeValue;
                    break;
                case "estimatedWorkTime":
                    $scope.estimatedWorkTime = Math.floor(identifier[i].childNodes[0].nodeValue/60) + " Hours - " + identifier[i].childNodes[0].nodeValue%60 +" Minutes";
                    break;
                case "deadline":
                    $scope.deadline = identifier[i].childNodes[0].nodeValue;
                    break;
                case "githook":
                    $scope.githook = identifier[i].childNodes[0].nodeValue;
                    break;
                case "comments":
                    $scope.comments = identifier[i].childNodes[0].nodeValue;
                    break;
            }
        }

        $scope.taskElementsBody = new XMLSerializer().serializeToString(xmlDoc.getElementsByTagName("taskElements")[0]);
        var taskElementsXML = [];
        var taskElements=[];

        taskElementsXML=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement");

        for (i = 0; i < taskElementsXML.length; i++) {
            var taskElement={};
            taskElement.type=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("type")[0].firstChild.nodeValue.trim();
            taskElement.id=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim();
            switch (taskElement.type)
            {
            case "image":
                taskElement.code="<br><div class='thumbnail noHeader'><img src='"+xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("link")[0].firstChild.nodeValue.trim()+
                "'></div>";
                break;
            case "checkbox":
                taskElement.code="<br><div class='checkbox m-b-15'> <label> <input type='checkbox' id='" +
                    xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim()+
                    "' " +
                    (xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("status")[0].firstChild.nodeValue=="checked"?"checked":"")+
                    " value='" +
                    xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() +
                    "'><i class='input-helper'></i>" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() + "</label></div>";
                break;
            case "textbox":
                taskElement.code="<br><div class='row'><div class='col-sm-12'><div class='fg-line'><textarea class='form-control input-md description'' rows='4' cols='50' id='"+
                    xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim()+
                    "'>" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() + "</textarea></div></div></div>";
                break;
            case "file":
                taskElement.code="<br><div class='row'><div class='col-sm-12'><a class='btn bgm-teal btn-icon-text waves-effect waves-effect' href='" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("link")[0].firstChild.nodeValue.trim() + "' target='_blank'><i class='zmdi zmdi-save'></i>"+
                  xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("status")[0].firstChild.nodeValue.trim() +
                  "</a></div></div>";
                break;
            case "slider":
                $scope.values=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.split("|");
                var length=$scope.values.length;

                taskElement.code="<br><input type='range' min='0' max='" + (length-1) + "' ng-model='model.range' id='" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim() + "'><div>{{values[model.range]}}</div>";
                break;
            default:
                ErrorHandler.handle({status:"0", item:"XML does not conform to XSD. Wrong element type."});
                break;
            }
            taskElements.push(taskElement);
        }

        $scope.taskBody = new XMLSerializer().serializeToString(xmlDoc.getElementsByTagName("taskBody")[0]);

        $scope.taskBody=$scope.taskBody.replace("<taskBody>","");
        $scope.taskBody=$scope.taskBody.replace("</taskBody>","");

        //extract position of taskElements in body
        var pattern= /{taskElement:(\d+)}/g;
        var match;
        while (match=pattern.exec($scope.taskBody)){
            var start=match.index;
            var end=match.index + match[0].length;
            var insertElement={}
            taskElements.forEach(function(taskElement){
                if (taskElement.id==match[1]){
                    $scope.taskBody=$scope.taskBody.replace(match[0],taskElement.code);
                    $scope.trustedTaskBody = $scope.taskBody;
                }
            });
        }
    }, function(error){
        ErrorHandler.handle(error);
    });
}

function showDSL(ErrorHandler,template){
    var template={};
    var identifier;
    var body;

    template.title = "";
    parser = new DOMParser();
    //template.syntax=template.response.syntax;
    xmlDoc = parser.parseFromString(template.syntax,"text/xml");
    template.xml.identifier = xmlDoc.getElementsByTagName("identifier")[0].childNodes[1].nodeValue;
    identifier = xmlDoc.getElementsByTagName("identifier")[0].childNodes;

    for (i = 0; i < identifier.length; i++) {
        switch(identifier[i].nodeName) {
            case "title":
                template.title = identifier[i].childNodes[0].nodeValue;
                break;
            case "description":
                template.description = identifier[i].childNodes[0].nodeValue;
                break;
            case "estimatedWorkTime":
                template.estimatedWorkTime = Math.floor(identifier[i].childNodes[0].nodeValue/60) + " Hours - " + identifier[i].childNodes[0].nodeValue%60 +" Minutes";
                break;
            case "deadline":
                template.deadline = identifier[i].childNodes[0].nodeValue;
                break;
            case "githook":
                template.githook = identifier[i].childNodes[0].nodeValue;
                break;
            case "comments":
                template.comments = identifier[i].childNodes[0].nodeValue;
                break;
        }
    }

    template.xml.taskElementsBody = new XMLSerializer().serializeToString(xmlDoc.getElementsByTagName("taskElements")[0]);
    var taskElementsXML = [];
    template.taskElements=[];

    taskElementsXML=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement");

    for (i = 0; i < taskElementsXML.length; i++) {
        var taskElement={};
        taskElement.type=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("type")[0].firstChild.nodeValue.trim();
        taskElement.id=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim();
        switch (taskElement.type)
        {
        case "image":
            taskElement.code="<br><div class='thumbnail noHeader'><img src='"+xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("link")[0].firstChild.nodeValue.trim()+
            "'></div>";
            break;
        case "checkbox":
            taskElement.code="<br><div class='checkbox m-b-15'> <label> <input type='checkbox' id='" +
                xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim()+
                "' " +
                (xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("status")[0].firstChild.nodeValue=="checked"?"checked":"")+
                " value='" +
                xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() +
                "'><i class='input-helper'></i>" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() + "</label></div>";
            break;
        case "textbox":
            taskElement.code="<br><div class='row'><div class='col-sm-12'><div class='fg-line'><textarea class='form-control input-md description'' rows='4' cols='50' id='"+
                xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim()+
                "'>" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.trim() + "</textarea></div></div></div>";
            break;
        case "file":
            taskElement.code="<br><div class='row'><div class='col-sm-12'><a class='btn bgm-teal btn-icon-text waves-effect waves-effect' href='" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("link")[0].firstChild.nodeValue.trim() + "' target='_blank'><i class='zmdi zmdi-save'></i>"+
              xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("status")[0].firstChild.nodeValue.trim() +
              "</a></div></div>";
            break;
        case "slider":
            $scope.values=xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getElementsByTagName("value")[0].firstChild.nodeValue.split("|");
            var length=$scope.values.length;

            taskElement.code="<br><input type='range' min='0' max='" + (length-1) + "' ng-model='model.range' id='" + xmlDoc.getElementsByTagName("taskElements")[0].getElementsByTagName("taskElement")[i].getAttribute("id").trim() + "'><div>{{values[model.range]}}</div>";
            break;
        default:
            ErrorHandler.handle({status:"0", item:"XML does not conform to XSD. Wrong element type."});
            break;
        }
        template.taskElements.push(taskElement);
    }

    template.xml.taskBody = new XMLSerializer().serializeToString(xmlDoc.getElementsByTagName("taskBody")[0]);

    template.xml.taskBody=template.xml.taskBody.replace("<taskBody>","");
    template.xml.taskBody=template.xml.taskBody.replace("</taskBody>","");

    //extract position of taskElements in body
    var pattern= /{taskElement:(\d+)}/g;
    var match;
    while (match=pattern.exec(template.xml.taskBody)){
        var start=match.index;
        var end=match.index + match[0].length;
        var insertElement={}
        template.taskElements.forEach(function(taskElement){
            if (taskElement.id==match[1]){
                template.xml.taskBody=template.xml.taskBody.replace(match[0],taskElement.code);
                template.trustedTaskBody = template.taskBody;
            }
        });
    }
    return template;
}