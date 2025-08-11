import http from 'k6/http';
import { check } from 'k6';

export let options = {
    vus: 100,
    duration: '30s',
};

const query = `
query {
  lectures {
    id
    title
    description
    enrolled
    instructor {
      id
      name
      email
    }
  }
}
`;

export default function () {
    const res = http.post('http://localhost:8080/graphql', JSON.stringify({ query }), {
        headers: { 'Content-Type': 'application/json' }
    });
    check(res, { 'status 200': (r) => r.status === 200 });
}
