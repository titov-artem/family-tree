var Person = Backbone.Model.extend({
    urlRoot: window.Endpoint.personRoot(),
    defaults: {
        id: 0,
        name: "None",
        surname: "None",
        description: "None",
        mainPhoto: ""
    }
});

var PersonList = Backbone.Collection.extend({
    model: Person,
    url: window.Endpoint.personRoot()
});

var personsSuggest = new PersonList();
personsSuggest.fetch({
    success: function () {
        var options = [];
        personsSuggest.each(function (e) {
            options.push({
                id: e.attributes.id,
                name: e.attributes.name + " " + e.attributes.surname
            });
        });
        $("#person1").selectize({
            valueField: 'id',
            labelField: 'name',
            searchField: 'name',
            options: options,
            create: true
        });
        $("#person2").selectize({
            valueField: 'id',
            labelField: 'name',
            searchField: 'name',
            options: options,
            create: true
        });
    }
});

var PersonTreeView = Backbone.View.extend({
    tagName: 'div',
    className: 'list-group-item',
    initialize: function () {
        this.model.on('change', this.render, this);
        this.render();
    },
    render: function () {
        this.$el.html(ich.person(this.model.toJSON()));
    },
    events: {
        "click .remove-person": "removePerson"
    },
    removePerson: function () {
        console.log('Removing person with id ' + this.model.get('id'));
        this.model.destroy();
        this.$el.remove();
    }
});

var Relation = Backbone.Model.extend({
    defaults: {
        familyId: -1,
        person1: -1,
        person2: -1,
        relationType: 'PARENT_CHILD'
    }
});

$(document).ready(function () {
    $('#add-relation').click(function () {
        var data = 'familyId=' + $('#familyId').val()
            + '&person1=' + $('#person1').val()
            + '&person2=' + $('#person2').val()
            + '&relationType=' + $('.relation-type.active > input').val();
        //var data = new Relation({
        //    familyId: $('#familyId').val(),
        //    person1: $('#person1').val(),
        //    person2: $('#person2').val(),
        //    relationType: $('.relation-type.active > input').val()
        //})
        $.ajax({
            type: 'POST',
            contentType: "application/json",
            data: '',
            url: window.Endpoint.familyRelation() + '?' + data
        });
    });
});