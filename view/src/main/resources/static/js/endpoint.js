/**
 * Created by scorpion on 03.07.15.
 */

window.Endpoint = {
    APIRoot: "http://localhost:8090/services/api",
    personRoot: function () {
        return this.APIRoot + "/person";
    },
    familyRoot: function () {
        return this.APIRoot + "/family";
    },
    familyRelation: function() {
        return this.familyRoot() + "/add/relation"
    }
};
