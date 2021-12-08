nonRepetead = function (){
    var fullNumber = db.phones.find({},{"display": 1, "_id": 0}).toArray();

    var nonRepeating = []
    for (var index = 0; index < fullNumber.length; index++) {
        var element = fullNumber[index].display;

        element = element.split("-")[1]
        var tempNumber = []
        var nonRepetead = true;
        for (var idx = 0; idx < element.length; idx++) {
            if (tempNumber.includes(element[idx])) {
                nonRepetead = false
                break
            }
            tempNumber.push(element[idx])
            
        }

        if (nonRepetead){
            nonRepeating.push(fullNumber[index])
        }

    }

    return nonRepeating
}