import React from 'react';
import './css/Section.css';

class Section extends React.Component {
  render() {
    return (
      <div className="section">
        <button className={this.props.currentSection === 'chat' ? 'button selected' : 'button not-selected'} onClick={this.chatButtonClicked}>Archived</button>
        <br></br>
        <button className={this.props.currentSection === 'all' ? 'button selected' : 'button not-selected'} onClick={this.allButtonClicked}>All</button>
        <br></br>
        <button className={this.props.currentSection === 'blocked' ? 'button selected' : 'button not-selected'} onClick={this.blockedButtonClicked}>Blocked</button>
      </div>
    );
  }

    chatButtonClicked = () => {
        this.props.setCurrentSection('chat');
    }

    allButtonClicked = () => {
        this.props.setCurrentSection('all');
    }

    blockedButtonClicked = () => {
        this.props.setCurrentSection('blocked');
    }
}

export default Section;