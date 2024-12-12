var notes = [
  {id: 1, title: 'Plan', content: 'what to do', tag: 'tip', createTime: "2018-1-1T07:10:10Z"},
  {id: 2, title: 'Do', content: 'do one thing one time', tag: 'tip', createTime: "2018-1-1T08:10:10Z"},
  {id: 3, title: 'Check', content: 'review and organize', tag: 'tip', createTime: "2018-1-1T09:10:10Z"},
  {id: 3, title: 'Action', content: 'adjust plan and do', tag: 'tip', createTime: "2018-1-1T10:10:10Z"}
];

function findNote (noteId) {
  return notes[findNoteKey(noteId)];
};

function findNoteKey (noteId) {
  for (var key = 0; key < notes.length; key++) {
    if (notes[key].id == noteId) {
      return key;
    }
  }
};

var List = Vue.extend({
  template: '#note-list',
  data: function () {
    return {notes: notes, searchKey: ''};
  },
  computed: {
    filteredNotes: function () {
      return this.notes.filter(function (note) {
        return this.searchKey=='' || note.title.indexOf(this.searchKey) !== -1;
      },this);
    }
  }
});

var Note = Vue.extend({
  template: '#note',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  }
});

var NoteEdit = Vue.extend({
  template: '#note-edit',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  },
  methods: {
    updateNote: function () {
      var note = this.note;
      notes[findNoteKey(note.id)] = {
        id: note.id,
        title: note.title,
        content: note.content,
        tag: note.tag
      };
      router.push('/');
    }
  }
});

var NoteDelete = Vue.extend({
  template: '#note-delete',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  },
  methods: {
    deleteNote: function () {
      notes.splice(findNoteKey(this.$route.params.note_id), 1);
      router.push('/');
    }
  }
});

var AddNote = Vue.extend({
  template: '#add-note',
  data: function () {
    return {note: {title: '', content: '', tag: ''}}
  },
  methods: {
    createNote: function() {
      var note = this.note;
      notes.push({
        id: Math.random().toString().split('.')[1],
        title: note.title,
        content: note.content,
        tag: note.tag
      });
      router.push('/');
    }
  }
});

var router = new VueRouter({routes:[
  { path: '/', component: List},
  { path: '/note/:note_id', component: Note, title: 'note'},
  { path: '/add-note', component: AddNote},
  { path: '/note/:note_id/edit', component: NoteEdit, title: 'note-edit'},
  { path: '/note/:note_id/delete', component: NoteDelete, title: 'note-delete'}
]});

var app = new Vue({
  router:router
}).$mount('#app')