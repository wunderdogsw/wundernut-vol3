const

  map = [
    [2, 0],
    [1, 3],
    [2, 2],
    [3, 3],
    [0, 6],
    [1, 5],
    [2, 6],
    [3, 5],
    [4, 6]
  ],

  $ = (selector) => {
    return document.querySelector(selector);
  },

  html = (html) => {
    let element = document.createElement('div');
    element.innerHTML = html;
    return element.firstChild;
  },

  load = () => {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/combinations.json', true);
    xhr.addEventListener('load', () => view(xhr.responseText));
    xhr.send();
  },

  view = (json) => {

    let
      combinations = JSON.parse(json),
      list = $('#combinations ul');

    combinations.forEach(combination => {
      let
        string = combination.map(c => c[0]).join(', '),
        item = html(`<li>[ ${string} ]</li>`);

      list.appendChild(item);

      item.addEventListener('click', () => {
        combination.forEach((config, index) => {

          let
            pos = map[index],
            piece = $(`#${config[0]}`);

          piece.style.transform = `translate(${pos[0] * 100}px, ${pos[1] * 58}px) rotate(-${config[1] * 60}deg)`;

        });
      });
    });

  };

load();