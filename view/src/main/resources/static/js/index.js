var Person = Backbone.Model.extend({
    initialize: function (person) {
    },
    urlRoot: "http://localhost:8090/services/api/person",
    defaults: {
        name: "None",
        surname: "None",
        description: "None",
        mainPhoto: ""
    }
});

var PersonList = Backbone.Collection.extend({
    model: Person,
    url: "http://localhost:8090/services/api/person"
});

var PersonView = Backbone.View.extend({
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

var PersonListView = Backbone.View.extend({
    tagName: 'div',
    className: 'list-group',
    initialize: function () {
        this.listenTo(this.collection, 'reset', this.render);
        this.listenTo(this.collection, 'add', this.addOne);

        this.collection.fetch({
            collection: this.collection
        });
    },
    render: function () {
        this.collection.forEach(this.addOne, this);
    },
    addOne: function (person) {
        var personView = new PersonView({model: person});
        this.$el.append(personView.el);
    }
});

$(document).ready(function () {
    var persons = new PersonList();
    var personsView = new PersonListView({collection: persons});
    $('#persons-list').append(personsView.el);
    $('#add-person').click(function () {
        persons.create({
            name: $('#name').val(),
            surname: $('#surname').val(),
            description: $('#description').val(),
            mainPhoto: $('#mainPhoto').val()
        }, {wait: true});
    });
});