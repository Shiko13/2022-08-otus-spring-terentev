window.onload = function () {

    function resetAll() {
        resetBookForm();
        resetCommentForm();
        resetCommentsTable();
    }

    document.getElementById('btn-get-all-books').onclick = function () {
        getAllBooks()
            .then(() => resetAll());
    };

    document.getElementById('btn-reset-book-form').onclick = function () {
        resetBookForm();
    };

    document.getElementById('btn-save-book-form').onclick = function () {
        onBookFormSubmit();
    };

    function onBookGetComments(btn) {
        let row = btn.parentElement.parentElement;
        document.getElementById('book-id-for-comments-input').value = row.cells[0].innerHTML;
        getAllCommentsByBookId(row.cells[0].innerHTML)
            .then(() => resetCommentForm());
    }

    function onBookPrepareEdit(btn) {
        let row = btn.parentElement.parentElement;
        document.getElementById("book-id-input").value = row.cells[0].innerHTML;
        document.getElementById("book-title-input").value = row.cells[1].innerHTML;
        document.getElementById("book-author-select").value = row.cells[2].innerHTML;
        document.getElementById("book-genre-select").value = row.cells[4].innerHTML;
    }

    function onBookDelete(btn) {
        if (confirm('Are you sure to delete this record?')) {
            let row = btn.parentElement.parentElement;
            deleteBookById(row.cells[0].innerHTML)
                .then(() => resetAll())
                .then(() => getAllBooks());
        }
    }

    function resetBookForm() {
        document.getElementById("book-id-input").value = "";
        document.getElementById("book-title-input").value = "";
        document.getElementById("book-author-select").value = "";
        document.getElementById("book-genre-select").value = "";
    }

    function onBookFormSubmit() {
        let bookTitle = document.getElementById('book-title-input').value;
        let authorId = Number(document.getElementById("book-author-select").querySelector("option:checked").value);
        let genreId = Number(document.getElementById("book-genre-select").querySelector("option:checked").value);

        let book = {
            title: bookTitle,
            author: {
                id: authorId
            },
            genre: {
                id: genreId
            }
        };

        console.log(JSON.stringify(book));

        let bookIdTxt = document.getElementById('book-id-input').value;

        if (!bookIdTxt) {
            createBook(book)
                .then(() => resetAll())
                .then(() => getAllBooks());
        } else {
            book["id"] = Number(bookIdTxt);
            updateBook(book)
                .then(() => resetAll())
                .then(() => getAllBooks());
        }
    }

    async function getAllBooks() {
        return await fetch('/api/books', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
            .then((response) => {
                response.json().then(
                    books => {
                        console.log(books);
                        let new_tbody = document.createElement('tbody');
                        if (books.length > 0) {
                            books.forEach((book) => {
                                let row = new_tbody.insertRow();
                                let cell0 = row.insertCell(0);
                                cell0.innerHTML = book.id;
                                let cell1 = row.insertCell(1);
                                cell1.innerHTML = book.title;
                                let cell2 = row.insertCell(2);
                                cell2.innerHTML = String(book.author.id);
                                let cell3 = row.insertCell(3);
                                cell3.innerHTML = book.author.name;
                                let cell4 = row.insertCell(4);
                                cell4.innerHTML = String(book.genre.id);
                                let cell5 = row.insertCell(5);
                                cell5.innerHTML = book.genre.name;
                                let cell6 = row.insertCell(6);
                                cell6.innerHTML = `<button id="btn-edit-book">Edit</button>`;
                                let cell7 = row.insertCell(7);
                                cell7.innerHTML = `<button id="btn-delete-book">Delete</button>`;
                                let cell8 = row.insertCell(8);
                                cell8.innerHTML = `<button id="btn-get-comments">Comments</button>`;
                            });
                        }
                        let old_tbody = document.getElementById('tab-books-data').getElementsByTagName('tbody')[0];
                        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
                        document.querySelectorAll('[id=btn-edit-book]').forEach((btn) => {
                            btn.addEventListener('click', function () {
                                onBookPrepareEdit(this);
                            });
                        });
                        document.querySelectorAll('[id=btn-delete-book]').forEach((btn) => {
                            btn.addEventListener('click', function () {
                                onBookDelete(this);
                            });
                        });
                        document.querySelectorAll('[id=btn-get-comments]').forEach((btn) => {
                            btn.addEventListener('click', function () {
                                onBookGetComments(this);
                            });
                        });
                    }
                )
            });
    }

    async function deleteBookById(id) {
        return await fetch('/api/books/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        });
    }

    async function updateBook(book) {
        return await fetch('/api/books/' + book.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(book)
        });
    }

    async function createBook(book) {
        return await fetch('/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(book)
        });
    }

    document.getElementById('btn-reset-comment-form').onclick = function () {
        resetCommentForm();
    };

    document.getElementById('btn-save-comment-form').onclick = function () {
        onCommentFormSubmit();
    };

    function onCommentPrepareEdit(btn) {
        let row = btn.parentElement.parentElement;
        document.getElementById("comment-id-input").value = row.cells[0].innerHTML;
        document.getElementById("comment-text-input").value = row.cells[1].innerHTML;
    }

    function onCommentDelete(btn) {
        if (confirm('Are you sure to delete this record?')) {
            let row = btn.parentElement.parentElement;
            let commentId = Number(row.cells[0].innerHTML);
            let bookId = Number(document.getElementById('book-id-for-comments-input').value);
            deleteCommentById(bookId, commentId)
                .then(() => resetCommentForm())
                .then(() => getAllCommentsByBookId(bookId));
        }
    }

    function resetCommentForm() {
        document.getElementById("comment-id-input").value = "";
        document.getElementById("comment-text-input").value = "";
    }

    function resetCommentsTable() {
        let old_tbody = document.getElementById('tab-comments-data').getElementsByTagName('tbody')[0];
        old_tbody.parentNode.replaceChild(document.createElement('tbody'), old_tbody);
        document.getElementById('book-id-for-comments-input').value = ""
    }

    function onCommentFormSubmit() {
        let commentText = document.getElementById('comment-text-input').value;
        let bookId = Number(document.getElementById('book-id-for-comments-input').value);

        let comment = {
            text: commentText,
            book: {
                id: bookId
            }
        };

        console.log(JSON.stringify(comment));

        let commentIdTxt = document.getElementById('comment-id-input').value;

        if (!commentIdTxt) {
            createComment(bookId, comment)
                .then(() => resetCommentForm())
                .then(() => getAllCommentsByBookId(bookId));
        } else {
            comment["id"] = Number(commentIdTxt);
            updateComment(bookId, comment)
                .then(() => resetCommentForm())
                .then(() => getAllCommentsByBookId(bookId));
        }
    }

    async function getAllCommentsByBookId(bookId) {
        return await fetch('/api/books/' + bookId + "/comments", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
            .then((response) => {
                response.json().then(
                    comments => {
                        console.log(comments);
                        let new_tbody = document.createElement('tbody');
                        if (comments.length > 0) {
                            comments.forEach((comment) => {
                                let row = new_tbody.insertRow();
                                let cell0 = row.insertCell(0);
                                cell0.innerHTML = comment.id;
                                let cell1 = row.insertCell(1);
                                cell1.innerHTML = comment.text;
                                let cell2 = row.insertCell(2);
                                cell2.innerHTML = `<button id="btn-edit-comment">Edit</button>`;
                                let cell3 = row.insertCell(3);
                                cell3.innerHTML = `<button id="btn-delete-comment">Delete</button>`;
                            });
                        }
                        let old_tbody = document.getElementById('tab-comments-data').getElementsByTagName('tbody')[0];
                        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
                        document.querySelectorAll('[id=btn-edit-comment]').forEach((btn) => {
                            btn.addEventListener('click', function () {
                                onCommentPrepareEdit(this);
                            });
                        });
                        document.querySelectorAll('[id=btn-delete-comment]').forEach((btn) => {
                            btn.addEventListener('click', function () {
                                onCommentDelete(this);
                            });
                        });
                    }
                )
            });
    }

    async function deleteCommentById(bookId, commentId) {
        return await fetch('/api/books/' + bookId + "/comments/" + commentId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        });
    }

    async function updateComment(bookId, comment) {
        return await fetch('/api/books/' + bookId + "/comments/" + comment.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(comment)
        });
    }

    async function createComment(bookId, comment) {
        return await fetch('/api/books/' + bookId + "/comments", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(comment)
        });
    }
}