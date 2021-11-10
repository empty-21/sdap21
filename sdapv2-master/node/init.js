/*
sudo -u postgres createuser sdap
sudo su - postgres
psql postgres

CREATE USER sdap PASSWORD 'sdap' SUPERUSER;
create database "sdap_dev" OWNER sdap;
create database "sdap_prod" OWNER sdap;

[docker]
mkdir -p ~/docker/data/pgdata
sudo chown -R 200:200 ~/docker
sudo chmod 775 -R ~/docker

docker run -d \
--name postgre \
-p 5432:5432 \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v ~/docker/data/pgdata:/var/lib/postgresql/data/pgdata \
postgres:13.2

docker exec -it postgre psql -U postgres
*/

var pg = require('pg');
var fs = require('fs');
const path = require('path');

const client = new pg.Client({
    host: '127.0.0.1',
    user: 'sdap',
    password: 'sdap',
    database: 'sdap_prod'
});


function files() {
    var files = [];
    ['../src/main/resources/sql/ddl/',
        '../src/main/resources/sql/ddl/migration/'].forEach((d, i, a) => {
            fs.readdirSync(d)
                .filter((file) => {
                    if (fs.lstatSync(path.join(a[i], file)).isFile()) {
                        files.push(a[i] + '' + file);
                    }
                });
        });
    return files;
}

function recreate(fun) {
    const which = files();
    var queries = [];

    which.forEach(f => {
        console.log('[read to sql file]', f);
        queries = queries.concat(fs.readFileSync(f).toString()
            .replace(/(\/\*[^*]*\*\/)|(\/\/[^*]*)|(--[^.].*)/gm, '')
            .replace(/^\s*\n/gm, "")
            .replace(/^\s+/gm, "")
            .replace(/(\r\n|\n|\r)/gm, " ") // remove newlines
            .replace(/\s+/g, ' ') // excess white space
            .split(";") // split into all statements
            .map(Function.prototype.call, String.prototype.trim)
            .filter(function (el) { return el.length != 0 }));
    });
    fun(queries);
    // console.log(queries);
}



client.connect(err => {
    if (err) {
        console.log('Failed to connect db ' + err);
        process.exit(1);
    } else {
        recreate(queries => {
            console.log('[[[[[[[[[[ start run query! ]]]]]]]]]]');
            queries.forEach((q, i) => {
                client.query(q, [], (e, res) => {
                    if (e) {
                        console.error('[error]', e.message);
                    } else {
                        console.log('[success]', q);
                    }
                    if (i == queries.length - 1) {
                        console.log('[ working done :) ]');
                        process.exit(1);
                    }
                });
            });
        })
    }
});